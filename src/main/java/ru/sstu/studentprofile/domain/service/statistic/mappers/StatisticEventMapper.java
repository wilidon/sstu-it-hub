package ru.sstu.studentprofile.domain.service.statistic.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.event.Event;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.domain.service.statistic.dto.StatisticProjectOut;
import ru.sstu.studentprofile.domain.service.statistic.dto.StatisticsEventOut;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatisticEventMapper {
    @Mapping(target = "membersCount", expression = "java((long) event.getEventProjects().size())")
    StatisticsEventOut toEventOut(Event event);
}
