package ru.sstu.studentprofile.domain.service.request.dto;

public record SenderOut (
        String login,
        String email,
        String lastName,
        String firstName,
        String middleName
) {
}
