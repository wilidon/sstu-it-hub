package ru.sstu.studentprofile.domain.service.project.dto;

import java.time.LocalDateTime;

public record ProjectOut (
    long id,
    String avatar,
    String name,
    String description,
    String status,
    LocalDateTime createDate
) {
}
