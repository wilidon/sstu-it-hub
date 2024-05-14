package ru.sstu.studentprofile.domain.service.statistic.dto;

import java.util.List;

public record StatisticsHotOut(
        List<StatisticHotProjectOut> lastCreatedProjects,
        StatisticsEventOut topEvent,
        StatisticRoleOut topRole,
        StatisticRoleOut rareRole,
        StatisticRoleOut findRole
) {
}
