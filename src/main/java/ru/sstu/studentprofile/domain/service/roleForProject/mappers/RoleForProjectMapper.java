package ru.sstu.studentprofile.domain.service.roleForProject.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.project.ActualRoleForProject;
import ru.sstu.studentprofile.data.models.project.RoleForProject;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectActualRoleOut;
import ru.sstu.studentprofile.domain.service.roleForProject.dto.RoleForProjectOut;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleForProjectMapper {
    RoleForProjectOut toRoleForProjectOut(RoleForProject role);
    List<RoleForProjectOut> toRoleForProjectOut(List<RoleForProject> role);
}
