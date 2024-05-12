package ru.sstu.studentprofile.domain.service.request.dto;

public record RequestOut(
        SenderOut sender,
        long recipient_id,
        long project_id,
        String type,
        String result
) {
}
