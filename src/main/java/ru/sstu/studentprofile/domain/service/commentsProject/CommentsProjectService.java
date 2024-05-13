package ru.sstu.studentprofile.domain.service.commentsProject;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.sstu.studentprofile.data.models.commentsProject.CommentsProject;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.data.repository.commentsProject.CommentsProjectRepository;
import ru.sstu.studentprofile.data.repository.project.ProjectRepository;
import ru.sstu.studentprofile.data.repository.user.UserRepository;
import ru.sstu.studentprofile.domain.exception.ForbiddenException;
import ru.sstu.studentprofile.domain.exception.NotFoundException;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.commentsProject.dto.CommentProjectIn;
import ru.sstu.studentprofile.domain.service.commentsProject.dto.CommentProjectOut;
import ru.sstu.studentprofile.domain.service.commentsProject.mappers.CommentProjectMapper;
import ru.sstu.studentprofile.domain.service.commentsProject.mappers.CommentProjectUserMapper;
import ru.sstu.studentprofile.domain.service.util.PageableOut;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentsProjectService {
    private final CommentsProjectRepository commentsProjectRepository;
    private final ProjectRepository projectRepository;
    private final CommentProjectMapper mapper;
    private final CommentProjectUserMapper mapperUser;
    private final UserRepository userRepository;

    public PageableOut<CommentProjectOut> getCommentsByProjectId(int page, int limit, long projectId){
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createDate").descending());
        Page<CommentsProject> comments = commentsProjectRepository.getCommentsProjectByProject(pageable, projectId);

        List<CommentProjectOut> commentsOut = new ArrayList<>();

        for (CommentsProject comment : comments.getContent()){
            commentsOut.add(mapper.toCommentProjectOut(comment, mapperUser));
        }

        return new PageableOut<>(
                page,
                comments.getSize(),
                comments.getTotalPages(),
                comments.getTotalElements(),
                commentsOut
        );
    }

    @Transactional
    public CommentProjectOut create(CommentProjectIn commentProjectIn, long projectId, JwtAuthentication authentication){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Проект с id=%d не найден".formatted(projectId)));
        User author = userRepository.findById(authentication.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(authentication.getUserId())));

        CommentsProject comment = mapper.toCommentProject(commentProjectIn);
        comment.setAuthor(author);
        comment.setProject(project);

        commentsProjectRepository.save(comment);

        return mapper.toCommentProjectOut(comment, mapperUser);
    }

    @Transactional
    public CommentProjectOut delete(long commentId, JwtAuthentication authentication){
        User author = userRepository.findById(authentication.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(authentication.getUserId())));

        CommentsProject comment = commentsProjectRepository.findById(commentId)
            .orElseThrow(() -> new NotFoundException("Комментарий с id=%d не найден".formatted(commentId)));

        if (author.getId() != comment.getAuthor().getId())
            throw new ForbiddenException("Вы не имеете права");

        commentsProjectRepository.deleteById(commentId);

        return mapper.toCommentProjectOut(comment, mapperUser);
    }
}
