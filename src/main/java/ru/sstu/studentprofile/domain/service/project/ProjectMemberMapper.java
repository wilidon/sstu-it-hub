package ru.sstu.studentprofile.domain.service.project;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.project.ProjectMember;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectIn;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectMemberOut;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectOut;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectMemberMapper {
    @Mapping(target = "id", expression = "java(projectMember.getId())")
    @Mapping(target = "login", expression = "java(projectMember.getUser().getLogin())")
    @Mapping(target = "email", expression = "java(projectMember.getUser().getEmail())")
    @Mapping(target = "rolesForProject", expression = "java(projectMember.getProjectMemberRoles().stream().map(role -> role.getRole().getName()).toList())")
    ProjectMemberOut toProjectMemberOut(ProjectMember projectMember);
}
