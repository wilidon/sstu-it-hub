package ru.sstu.studentprofile.domain.service.request.dto;

import ru.sstu.studentprofile.domain.service.project.dto.ProjectOut;

import java.time.LocalDateTime;

public record RequestOut(
        long id,
        SenderOut sender,
        long recipient_id,
        ProjectOut project,
        String type,
        String result,
        LocalDateTime createdAt
) {
}
