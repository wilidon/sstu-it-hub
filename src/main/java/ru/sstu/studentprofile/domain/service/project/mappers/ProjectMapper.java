package ru.sstu.studentprofile.domain.service.project.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectIn;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectOut;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectMapper {
    @Mapping(target = "actualRoles", expression = "java(project.getActualRoleForProject().stream().map(actualRole -> mapperProjectActualRole.toProjectActualRoleOut(actualRole)).toList())")
    @Mapping(target = "members", expression = "java(project.getProjectMembers().stream().map(member -> mapperProjectMember.toProjectMemberOut(member)).toList())")
    @Mapping(target = "event", expression = "java(mapperProjectEvent.toProjectEventOut(project.getEvent()))")
    ProjectOut toProjectOut(Project project, ProjectMemberMapper mapperProjectMember, ProjectActualRoleMapper mapperProjectActualRole, ProjectEventMapper mapperProjectEvent);

    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "id", ignore = true)
    Project toProject(ProjectIn projectIn, User user);
}
