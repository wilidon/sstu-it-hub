package ru.sstu.studentprofile.domain.service.statistic.dto;

public record StatisticHotProjectOut(
        long id,
        String avatar,
        String name,
        String description,
        String status
) {
}
