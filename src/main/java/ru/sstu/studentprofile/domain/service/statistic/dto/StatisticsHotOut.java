package ru.sstu.studentprofile.domain.service.statistic.dto;

import java.util.List;

public record StatisticsHotOut(
        List<StatisticHotProjectOut> lastCreatedProjects,
        StatisticsEventOut topEvent,
//        StatisticUserOut topMember,
//        StatisticUserOut topLeader,
        String topRole,
        String rareRole,
        String findRole
) {
}
