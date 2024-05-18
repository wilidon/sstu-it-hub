package ru.sstu.studentprofile.domain.service.project.comment.dto;

import java.time.LocalDateTime;

public record ProjectCommentOut(
        long id,
        ProjectCommentUserOut author,
        String text,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isEdited) {
}
