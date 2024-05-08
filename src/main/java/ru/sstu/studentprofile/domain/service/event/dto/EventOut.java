package ru.sstu.studentprofile.domain.service.event.dto;


public record EventOut(
        long id,
        String avatar,
        String name,
        String description,
        String startDate,
        String endDate,
        String status,
        Long membersCount
) {

}
