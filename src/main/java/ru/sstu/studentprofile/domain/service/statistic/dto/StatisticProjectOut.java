package ru.sstu.studentprofile.domain.service.statistic.dto;

public record StatisticProjectOut(
        long countAllProject,
        long countAllProjectToday,
        long countAllProjectPlanned,
        long countAllProjectOpen,
        long countAllProjectComleted
) {
}
