package ru.sstu.studentprofile.domain.service.project.dto;

import java.util.List;
import java.util.Set;

public record ProjectMemberOut(
        long id,
        String login,
        String email,
        List<String> rolesForProject
) {
}
