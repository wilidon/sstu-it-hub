package ru.sstu.studentprofile.data.models.request;

import ru.sstu.studentprofile.domain.service.request.dto.RequestResultIn;
import ru.sstu.studentprofile.domain.service.request.dto.RequestTypeIn;

public enum RequestResult {
    REFUSED,
    ACEEPTED,
    WAIT;

    public static RequestResult fromRequestResultIn(RequestResultIn requestResultIn) {
        return switch (requestResultIn) {
            case REFUSED -> REFUSED;
            case ACEEPTED -> ACEEPTED;
            case WAIT -> WAIT;
            default -> throw new IllegalArgumentException("Неизвестный статус проекта: " + requestResultIn);
        };
    }

}
