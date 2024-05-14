package ru.sstu.studentprofile.domain.service.user.dto;

import ru.sstu.studentprofile.data.models.project.RoleForProject;
import ru.sstu.studentprofile.domain.service.roleForProject.dto.RoleForProjectOut;
import ru.sstu.studentprofile.domain.service.user.dto.rating.UserRatingOut;

import java.util.List;

public record UserOut(
        long id,
        String login,
        String email,
        String avatar,
        String background,
        String lastName,
        String firstName,
        String middleName,
        List<String> roles,
        List<RoleForProjectOut> rolesForProject,
        List<UserReviewOut> reviews,
        UserMediaOut media,
        UserRatingOut ratings
) {

}
