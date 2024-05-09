package ru.sstu.studentprofile.data.models.event;

import ru.sstu.studentprofile.domain.service.event.dto.EventStatusIn;

public enum EventStatus {
    PLANNED, // Запланировано
    OPEN, // идет в настоящее время
    COMPLETED // завершено
    ;

    public static EventStatus fromEventStatusIn(EventStatusIn eventStatusIn) {
        return switch (eventStatusIn) {
            case PLANNED -> PLANNED;
            case OPEN -> OPEN;
            case COMPLETED -> COMPLETED;
            default -> throw new IllegalArgumentException("Неизвестный статус мероприятия: " + eventStatusIn);
        };
    }
}
