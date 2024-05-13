package ru.sstu.studentprofile.domain.service.commentsProject.dto;

public record CommentProjectOut (
        long id,
        CommentProjectUserOut author,
        String text,
        String createDate
) {
}
