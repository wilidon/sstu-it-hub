package ru.sstu.studentprofile.web.controller.auth.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sstu.studentprofile.domain.service.auth.AuthService;
import ru.sstu.studentprofile.domain.service.auth.dto.BearerLoginOut;
import ru.sstu.studentprofile.domain.service.auth.dto.LoginIn;
import ru.sstu.studentprofile.domain.service.auth.dto.RefreshAccessTokenIn;
import ru.sstu.studentprofile.domain.service.auth.dto.RegisterIn;
import ru.sstu.studentprofile.web.controller.auth.AuthApi;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "1. Авторизация")
public class AuthController implements AuthApi {
    private AuthService authService;

    @Override
    public ResponseEntity<BearerLoginOut> login(final @RequestBody LoginIn loginIn) {
        return ResponseEntity.ok(authService.login(loginIn));
    }

    @Override
    public ResponseEntity<BearerLoginOut> register(final @RequestBody RegisterIn registerIn) {
        return ResponseEntity.ok(authService.register(registerIn));
    }

    @Override
    public ResponseEntity<?> refresh(final @RequestBody RefreshAccessTokenIn refreshAccessTokenIn) {
        return ResponseEntity.ok(authService.refresh(refreshAccessTokenIn));
    }
}
