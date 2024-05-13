package ru.sstu.studentprofile.domain.service.commentsProject.dto;

import java.time.LocalDateTime;

public record CommentProjectIn (
        String text,
        LocalDateTime createDate
) {
}
