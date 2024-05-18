package ru.sstu.studentprofile.domain.service.project.comment.dto;

public record ProjectCommentUserOut(
        long id,
        String login,
        String email,
        String avatar
){
}
