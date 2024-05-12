package ru.sstu.studentprofile.domain.service.statistic.dto;

import java.util.List;

public record StatisticUserOut (
        long id,
        String login,
        String avatar,
        String lastName,
        String firstName,
        String middleName,
        int countProjectLeader,
        int countProjectMember
){
}
