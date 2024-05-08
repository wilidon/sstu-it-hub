package ru.sstu.studentprofile.domain.service.auth.dto;

import jakarta.validation.constraints.NotNull;

public record CheckUsernameIn(@NotNull String value) {
}
