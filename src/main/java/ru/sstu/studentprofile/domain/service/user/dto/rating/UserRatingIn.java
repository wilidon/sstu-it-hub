package ru.sstu.studentprofile.domain.service.user.dto.rating;

import ru.sstu.studentprofile.data.models.user.UserRatingType;

import java.util.List;

public record UserRatingIn(
        List<UserRatingType> types
) {

}
