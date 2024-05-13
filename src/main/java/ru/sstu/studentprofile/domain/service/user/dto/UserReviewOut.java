package ru.sstu.studentprofile.domain.service.user.dto;

import ru.sstu.studentprofile.data.models.user.ReviewType;

import java.time.LocalDateTime;

public record UserReviewOut(
        long id,
        ShortUserOut author,
        ShortUserOut recipient,
        ReviewType reviewType,
        String review,
        LocalDateTime createdAt
) {

}