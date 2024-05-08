package ru.sstu.studentprofile.domain.security;

import org.springframework.security.core.Authentication;

public interface JwtAuthentication extends Authentication {
    long getUserId();
}
