package ru.sstu.studentprofile.domain.service.user.dto;

public record UserAboutIn (
    String vkUrl,
    String tgUrl,
    String phone,
    String about
) {

}
