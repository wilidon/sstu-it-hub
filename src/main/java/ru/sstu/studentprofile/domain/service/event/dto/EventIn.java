package ru.sstu.studentprofile.domain.service.event.dto;

import ru.sstu.studentprofile.data.models.event.EventStatus;

import java.time.LocalDateTime;

public record EventIn (
        String name,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        EventStatusIn status
) {
}
