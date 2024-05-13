package ru.sstu.studentprofile.domain.service.project.dto;

import java.util.List;

public record ProjectLeaderOut (
        long userId,
        String avatar,
        String login,
        String email,
        String lastName,
        String firstName,
        String middleName,
        String role
) {
}
