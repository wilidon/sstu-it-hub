package ru.sstu.studentprofile.domain.service.user.dto;

import ru.sstu.studentprofile.domain.service.project.dto.ProjectActualRoleOut;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectEventOut;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectMemberOut;

import java.time.LocalDateTime;
import java.util.List;

public record UserProject (
    long id,
    String avatar,
    String name,
    String description,
    String status,
    ProjectEventOut event,
    List<ProjectMemberOut> members,
    List<ProjectActualRoleOut> actualRoles,
    LocalDateTime createDate
) {

}
