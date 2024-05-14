package ru.sstu.studentprofile.domain.service.project.dto;

public record ProjectMediaOut (
        long id,
        String githubLink,
        String urlSite,
        String stack
) {

}
