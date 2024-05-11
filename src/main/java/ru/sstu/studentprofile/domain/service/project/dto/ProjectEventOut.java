package ru.sstu.studentprofile.domain.service.project.dto;

public record ProjectEventOut(
        long id,
        String name,
        String avatar,
        String description,
        String status
) {
}
