package ru.sstu.studentprofile.domain.service.event.dto;


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
        List<EventMemberOut> members
) {

}
