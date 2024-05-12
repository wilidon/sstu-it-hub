package ru.sstu.studentprofile.domain.service.request.dto;

public record RequestIn (
        Long projectId,
        Long recipientId,
        RequestTypeIn type
){
}
