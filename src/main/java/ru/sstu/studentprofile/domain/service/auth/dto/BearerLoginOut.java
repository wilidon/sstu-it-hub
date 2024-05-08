package ru.sstu.studentprofile.domain.service.auth.dto;

import java.util.Collection;

public record BearerLoginOut(
        String type,
        String accessToken,
        Long accessExpires,
        String refreshToken,
        Long refreshExpires,
        Collection<String> authorities) {
    private static final String TYPE = "Bearer";

    public BearerLoginOut(final String accessToken,
                          final Long accessExpires,
                          final String refreshToken,
                          final Long refreshExpires,
                          final Collection<String> authorities) {
        this(TYPE,
                accessToken,
                accessExpires,
                refreshToken,
                refreshExpires,
                authorities);
    }
}
