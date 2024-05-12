package ru.sstu.studentprofile.domain.service.statistic.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectIn;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectOut;
import ru.sstu.studentprofile.domain.service.project.mappers.ProjectActualRoleMapper;
import ru.sstu.studentprofile.domain.service.project.mappers.ProjectEventMapper;
import ru.sstu.studentprofile.domain.service.project.mappers.ProjectMemberMapper;
import ru.sstu.studentprofile.domain.service.statistic.dto.StatisticHotProjectOut;
import ru.sstu.studentprofile.domain.service.statistic.dto.StatisticProjectOut;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatisitcProjectMapper {
    StatisticProjectOut toProjectOut(Project project);
    List<StatisticHotProjectOut> toProjectOut(List<Project> project);

}
