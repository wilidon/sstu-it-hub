package ru.sstu.studentprofile.web.controller.project.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.project.comment.CommentsProjectService;
import ru.sstu.studentprofile.domain.service.project.comment.dto.ProjectCommentIn;
import ru.sstu.studentprofile.domain.service.project.comment.dto.ProjectCommentUpdateIn;
import ru.sstu.studentprofile.web.controller.project.ProjectCommentsApi;

@RestController
@AllArgsConstructor
public class ProjectCommentsController implements ProjectCommentsApi {
    private final CommentsProjectService commentsProjectService;

    @Override
    public ResponseEntity<?> getAll(long projectId, int page, int limit) {
        return ResponseEntity.ok(commentsProjectService.all(projectId, page, limit));
    }

    @Override
    public ResponseEntity<?> add(ProjectCommentIn projectCommentIn, long projectId, JwtAuthentication authentication) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentsProjectService.create(projectCommentIn, projectId, authentication));
    }

    @Override
    public ResponseEntity<?> update(ProjectCommentUpdateIn projectCommentUpdateIn, long projectId, long commentId, JwtAuthentication authentication) {
        return ResponseEntity.ok(commentsProjectService.update(projectCommentUpdateIn,
                projectId,
                commentId,
                authentication));
    }

    @Override
    public ResponseEntity<?> delete(long commentId, JwtAuthentication authentication) {
        commentsProjectService.delete(commentId, authentication);
        return ResponseEntity.noContent().build();
    }
}
