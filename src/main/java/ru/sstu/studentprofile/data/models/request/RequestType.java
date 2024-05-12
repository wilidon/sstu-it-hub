package ru.sstu.studentprofile.data.models.request;

import ru.sstu.studentprofile.domain.service.request.dto.RequestTypeIn;

public enum RequestType {
    INVITE,
    REQUEST;

    public static RequestType fromRequestTypeIn(RequestTypeIn requestTypeIn) {
        return switch (requestTypeIn) {
            case INVITE -> INVITE;
            case REQUEST -> REQUEST;
            default -> throw new IllegalArgumentException("Неизвестный статус проекта: " + requestTypeIn);
        };
    }
}
