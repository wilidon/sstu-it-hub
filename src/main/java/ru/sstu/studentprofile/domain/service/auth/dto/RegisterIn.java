package ru.sstu.studentprofile.domain.service.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;


public record RegisterIn(@NotNull @Size(max = 128) @Email @NotBlank String email,
                         @Size(max = 32) @NotNull @NotBlank String login,
                         @Size(min = 1, max = 128) @NotNull @NotBlank String lastName,
                         @Size(min = 1, max = 128) @NotNull @NotBlank String firstName,
                         @Size(max = 128) @NotBlank String middleName,
                         @NotNull @Size(max = 128) @NotBlank String password) implements Serializable {
}
