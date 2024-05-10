package ru.sstu.studentprofile.domain.service.project.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectOut (
    long id,
    String avatar,
    String name,
    String description,
    String status,

    List<String> actualRoles,
    LocalDateTime createDate
) {
}
