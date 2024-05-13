package ru.sstu.studentprofile.web.controller.commentsProject.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.commentsProject.CommentsProjectService;
import ru.sstu.studentprofile.domain.service.commentsProject.dto.CommentProjectIn;
import ru.sstu.studentprofile.domain.service.commentsProject.dto.CommentProjectOut;
import ru.sstu.studentprofile.domain.service.util.PageableOut;
import ru.sstu.studentprofile.web.controller.commentsProject.CommentsProjectApi;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class CommentsProjectController implements CommentsProjectApi {
    private final CommentsProjectService commentsProjectService;
    @Override
    public ResponseEntity<PageableOut<CommentProjectOut>> getCommentsByProjectId(int page, int limit, long projectId) {
        return ResponseEntity.ok(commentsProjectService.getCommentsByProjectId(page, limit, projectId));
    }

    @Override
    public ResponseEntity<?> create(CommentProjectIn commentProjectIn, long projectId, JwtAuthentication authentication) {
        return ResponseEntity.ok(commentsProjectService.create(commentProjectIn, projectId, authentication));
    }

    @Override
    public ResponseEntity<?> delete(long commentId, JwtAuthentication authentication) {
        return ResponseEntity.ok(commentsProjectService.delete(commentId, authentication));
    }
}
