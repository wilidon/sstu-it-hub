package ru.sstu.studentprofile.domain.service.statistic.dto;

public record StatisticOut (
        StatisticPeopleOut statisticPeople,
        StatisticProjectOut statisticProject
) {

}
