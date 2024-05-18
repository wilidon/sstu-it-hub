package ru.sstu.studentprofile.web.controller.commentsProject.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.project.comment.CommentsProjectService;
import ru.sstu.studentprofile.domain.service.project.comment.dto.ProjectCommentIn;
import ru.sstu.studentprofile.domain.service.project.comment.dto.ProjectCommentOut;
import ru.sstu.studentprofile.domain.service.util.PageableOut;
import ru.sstu.studentprofile.web.controller.commentsProject.CommentsProjectApi;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class CommentsProjectController implements CommentsProjectApi {
    private final CommentsProjectService commentsProjectService;
    @Override
    @Deprecated(forRemoval = true)
    public ResponseEntity<PageableOut<ProjectCommentOut>> getCommentsByProjectId(int page, int limit, long projectId) {
        return ResponseEntity.ok(commentsProjectService.all(projectId, page, limit));
    }

    @Override
    @Deprecated(forRemoval = true)
    public ResponseEntity<?> create(ProjectCommentIn projectCommentIn, long projectId, JwtAuthentication authentication) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentsProjectService.create(projectCommentIn, projectId, authentication));
    }

    @Override
    @Deprecated(forRemoval = true)
    public ResponseEntity<?> delete(long commentId, JwtAuthentication authentication) {
        commentsProjectService.delete(commentId, authentication);
        return ResponseEntity.noContent().build();
    }
}
