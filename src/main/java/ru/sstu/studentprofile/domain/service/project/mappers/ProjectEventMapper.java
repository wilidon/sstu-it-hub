package ru.sstu.studentprofile.domain.service.project.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.event.Event;
import ru.sstu.studentprofile.data.models.project.ActualRoleForProject;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectActualRoleOut;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectEventOut;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectEventMapper {
    ProjectEventOut toProjectEventOut(Event event);
}
