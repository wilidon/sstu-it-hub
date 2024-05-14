package ru.sstu.studentprofile.domain.service.project.dto;

public record ProjectMediaIn (
        long id,
        String githubLink,
        String urlSite,
        String stack
) {

}
