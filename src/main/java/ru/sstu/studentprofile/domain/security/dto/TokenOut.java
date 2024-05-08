package ru.sstu.studentprofile.domain.security.dto;

public record TokenOut(String token, Long expires) {
}
