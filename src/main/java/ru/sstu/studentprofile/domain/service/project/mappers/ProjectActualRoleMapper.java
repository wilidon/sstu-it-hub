package ru.sstu.studentprofile.domain.service.project.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.project.ActualRoleForProject;
import ru.sstu.studentprofile.data.models.project.RoleForProject;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectActualRoleOut;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectActualRoleMapper {
    @Mapping(target = "id", expression = "java(role.getRole().getId())")
    @Mapping(target = "name", expression = "java(role.getRole().getName())")
    ProjectActualRoleOut toProjectActualRoleOut(ActualRoleForProject role);
}
