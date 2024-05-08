package ru.sstu.studentprofile.domain.service.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckEmailIn(@NotNull @NotBlank @Email String value) {
}
