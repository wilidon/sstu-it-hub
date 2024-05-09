package ru.sstu.studentprofile.domain.service.event.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record EventIn (
        @Size(max = 128)
        String name,
        @Size(max = 2048)
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        EventStatusIn status
) {
}
