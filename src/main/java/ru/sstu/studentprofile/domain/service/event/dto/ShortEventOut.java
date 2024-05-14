package ru.sstu.studentprofile.domain.service.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortEventOut {
    long id;
    String avatar;
    String name;
    String description;
    String startDate;
    String endDate;
    String status;
    Long membersCount;

    public ShortEventOut(long id, String avatar, String name, String description, String startDate, String endDate, String status, Long membersCount) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.membersCount = membersCount;
    }
}
