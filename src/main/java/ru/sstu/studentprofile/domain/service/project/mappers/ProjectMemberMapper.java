package ru.sstu.studentprofile.domain.service.project.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.project.ProjectMember;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectIn;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectLeaderOut;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectMemberOut;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectOut;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectMemberMapper {
    @Mapping(target = "id", expression = "java(projectMember.getId())")
    @Mapping(target = "userId", expression = "java(projectMember.getUser().getId())")
    @Mapping(target = "avatar", expression = "java(projectMember.getUser().getAvatar())")
    @Mapping(target = "login", expression = "java(projectMember.getUser().getLogin())")
    @Mapping(target = "lastName", expression = "java(projectMember.getUser().getLastName())")
    @Mapping(target = "firstName", expression = "java(projectMember.getUser().getFirstName())")
    @Mapping(target = "middleName", expression = "java(projectMember.getUser().getMiddleName())")
    @Mapping(target = "rolesForProject", expression = "java(projectMember.getProjectMemberRoles().stream().map(role -> role.getRole().getName()).toList())")
    ProjectMemberOut toProjectMemberOut(ProjectMember projectMember);

    @Mapping(target = "userId", expression = "java(user.getId())")
    @Mapping(target = "avatar", expression = "java(user.getAvatar())")
    @Mapping(target = "login", expression = "java(user.getLogin())")
    @Mapping(target = "lastName", expression = "java(user.getLastName())")
    @Mapping(target = "firstName", expression = "java(user.getFirstName())")
    @Mapping(target = "middleName", expression = "java(user.getMiddleName())")
    ProjectLeaderOut toProjectLeaderOut(User user);
}
