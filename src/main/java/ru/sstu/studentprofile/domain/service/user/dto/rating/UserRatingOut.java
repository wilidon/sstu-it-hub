package ru.sstu.studentprofile.domain.service.user.dto.rating;

import ru.sstu.studentprofile.data.models.user.UserRatingType;

public record UserRatingOut (
        UserRatingType ratingType,
        long count
) {

}
