package ru.sstu.studentprofile.domain.service.request.dto;

public record SenderOut (
        long id,
        String avatar,
        String email,
        String lastName,
        String firstName,
        String middleName) {
}
