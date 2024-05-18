package ru.sstu.studentprofile.domain.service.project.comment;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.sstu.studentprofile.data.models.project.comment.ProjectComment;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.data.repository.commentsProject.CommentsProjectRepository;
import ru.sstu.studentprofile.data.repository.project.ProjectRepository;
import ru.sstu.studentprofile.data.repository.user.UserRepository;
import ru.sstu.studentprofile.domain.exception.ForbiddenException;
import ru.sstu.studentprofile.domain.exception.NotFoundException;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.project.comment.dto.ProjectCommentIn;
import ru.sstu.studentprofile.domain.service.project.comment.dto.ProjectCommentOut;
import ru.sstu.studentprofile.domain.service.project.comment.dto.ProjectCommentUpdateIn;
import ru.sstu.studentprofile.domain.service.project.comment.mappers.CommentProjectMapper;
import ru.sstu.studentprofile.domain.service.util.PageableOut;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CommentsProjectService {
    private final CommentsProjectRepository commentsProjectRepository;
    private final ProjectRepository projectRepository;
    private final CommentProjectMapper mapper;
    private final UserRepository userRepository;

    public PageableOut<ProjectCommentOut> all(long projectId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<ProjectComment> comments = commentsProjectRepository.getCommentsProjectByProject(projectId, pageable);


        return new PageableOut<>(
                pageable.getPageNumber(),
                comments.getSize(),
                comments.getTotalPages(),
                comments.getTotalElements(),
                mapper.toCommentProjectOut(comments.getContent())
        );
    }

    @Transactional
    public ProjectCommentOut create(ProjectCommentIn projectCommentIn, long projectId, JwtAuthentication authentication) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Проект с id=%d не найден".formatted(projectId)));
        User author = userRepository.findById(authentication.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(authentication.getUserId())));

        ProjectComment comment = mapper.toCommentProject(projectCommentIn);
        comment.setAuthor(author);
        comment.setProject(project);

        commentsProjectRepository.save(comment);

        return mapper.toCommentProjectOut(comment);
    }

    @Transactional
    public ProjectCommentOut update(ProjectCommentUpdateIn projectCommentUpdateIn,
                                    long projectId,
                                    long commentId,
                                    JwtAuthentication authentication) {
        projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Проект с id=%d не найден".formatted(projectId)));
        ProjectComment comment = commentsProjectRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id=%d не найден".formatted(commentId)));

        comment.setText(projectCommentUpdateIn.text());
        comment.setUpdatedAt(LocalDateTime.now());

        commentsProjectRepository.save(comment);

        return mapper.toCommentProjectOut(comment);
    }


    @Transactional
    public void delete(long commentId, JwtAuthentication authentication) {
        User author = userRepository.findById(authentication.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(authentication.getUserId())));

        ProjectComment comment = commentsProjectRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id=%d не найден".formatted(commentId)));

        if (author.getId() != comment.getAuthor().getId())
            throw new ForbiddenException("Вы не имеете права");

        commentsProjectRepository.deleteById(commentId);
    }
}
