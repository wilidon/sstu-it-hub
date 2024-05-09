package ru.sstu.studentprofile.domain.service.user.dto;

import java.util.List;

public record UserOut(
        long id,
        String login,
        String email,
        List<String> roles,
        List<String> rolesForProject
) {

}
