package ru.sstu.studentprofile.domain.service.user.dto;

public record UserMediaOut (
        String tgUrl,
        String vkUrl,
        String phone,
        String email,
        String about
        ) {
}
