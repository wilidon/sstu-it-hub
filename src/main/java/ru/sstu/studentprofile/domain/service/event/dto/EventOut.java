package ru.sstu.studentprofile.domain.service.event.dto;


import ru.sstu.studentprofile.data.models.event.EventWinner;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectOut;

import java.util.List;

public record EventOut(
        long id,
        String avatar,
        String name,
        String description,
        String startDate,
        String endDate,
        String status,
        Long membersCount,
        List<EventMemberOut> members,
        List<ProjectOut> projects,
        List<ProjectOut> winners
) {

}
