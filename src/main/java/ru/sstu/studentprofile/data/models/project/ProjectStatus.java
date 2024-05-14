package ru.sstu.studentprofile.data.models.project;

import ru.sstu.studentprofile.data.models.event.EventStatus;
import ru.sstu.studentprofile.domain.service.event.dto.EventStatusIn;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectStatusIn;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectStatusSearchIn;

public enum ProjectStatus {
    PLANNED,
    OPEN,
    COMPLETED;

    public static ProjectStatus fromProjectStatusIn(ProjectStatusIn projectStatusIn) {
        return switch (projectStatusIn) {
            case PLANNED -> PLANNED;
            case OPEN -> OPEN;
            case COMPLETED -> COMPLETED;
            default -> throw new IllegalArgumentException("Неизвестный статус проекта: " + projectStatusIn);
        };
    }

    public static ProjectStatus fromProjectStatusSearchIn(ProjectStatusSearchIn projectStatusSearchIn) {
        return switch (projectStatusSearchIn) {
            case PLANNED -> PLANNED;
            case OPEN -> OPEN;
            case COMPLETED -> COMPLETED;
            default -> throw new IllegalArgumentException("Неизвестный статус проекта: " + projectStatusSearchIn);
        };
    }
}
