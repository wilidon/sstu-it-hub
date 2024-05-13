package ru.sstu.studentprofile.domain.service.user.dto;

public record UserMediaIn(
    String vkUrl,
    String tgUrl,
    String phone
) {

}
