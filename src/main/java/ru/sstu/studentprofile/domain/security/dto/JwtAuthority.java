package ru.sstu.studentprofile.domain.security.dto;

import org.springframework.security.core.GrantedAuthority;

public record JwtAuthority(String name) implements GrantedAuthority {
    @Override
    public String getAuthority() {
        return name;
    }
}
