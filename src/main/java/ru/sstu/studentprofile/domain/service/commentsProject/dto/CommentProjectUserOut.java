package ru.sstu.studentprofile.domain.service.commentsProject.dto;

public record CommentProjectUserOut (
        long id,
        String login,
        String email,
        String avatar
){
}
