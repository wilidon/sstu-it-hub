package ru.sstu.studentprofile.domain.service.project.dto;

import java.util.List;
import java.util.Set;

public record ProjectMemberOut(
        long id,
        long userId,
        String avatar,
        String login,
        String email,
        String lastName,
        String firstName,
        String middleName,
        List<String> rolesForProject
) {
}
