package ru.sstu.studentprofile.domain.service.project.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectOut (
    long id,
    String avatar,
    String name,
    String description,
    ProjectLeaderOut leader,
    String status,
    ProjectEventOut event,
    List<ProjectMemberOut> members,
    List<ProjectActualRoleOut> actualRoles,
    LocalDateTime createDate,
    ProjectMediaOut media
) {
}
