package ru.sstu.studentprofile.data.repository.user.projection;

import ru.sstu.studentprofile.data.models.user.UserRatingType;

public interface UserRatingProjection {
    UserRatingType getRatingType();
    long getCount();
}
