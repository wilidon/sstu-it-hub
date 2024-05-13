package ru.sstu.studentprofile.domain.service.user.dto;

public record ShortUserOut (
        long id,
        String avatar,
        String lastName,
        String firstName,
        String middleName
) {

}
