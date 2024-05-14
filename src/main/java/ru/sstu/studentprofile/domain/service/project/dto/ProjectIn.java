package ru.sstu.studentprofile.domain.service.project.dto;

import java.time.LocalDateTime;

public record ProjectIn (
        String name,
        String description,
        String githubUrl,
        String siteUrl,
        String stack
        ) {
}
