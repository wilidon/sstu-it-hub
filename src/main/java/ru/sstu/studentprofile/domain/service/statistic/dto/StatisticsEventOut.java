package ru.sstu.studentprofile.domain.service.statistic.dto;

import ru.sstu.studentprofile.domain.service.event.dto.EventMemberOut;

import java.util.List;

public record StatisticsEventOut (
        long id,
        String avatar,
        String name,
        String description,
        String startDate,
        String endDate,
        String status,
        Long membersCount
){

}
