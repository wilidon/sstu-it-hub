package ru.sstu.studentprofile.web.controller.auth;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sstu.studentprofile.domain.service.auth.dto.BearerLoginOut;
import ru.sstu.studentprofile.domain.service.auth.dto.LoginIn;
import ru.sstu.studentprofile.domain.service.auth.dto.RefreshAccessTokenIn;
import ru.sstu.studentprofile.domain.service.auth.dto.RegisterIn;

@Tag(name = "1. Авторизация")
@RequestMapping("/auth")
@CrossOrigin("*")
public interface AuthApi {
    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает токен доступа и токен обновления. " +
                            "Дополнительно содержится информация о том, когда эти токены истекут и роль пользователя.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BearerLoginOut.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Неверный логин или пароль",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    ResponseEntity<BearerLoginOut> login(final @RequestBody LoginIn loginIn);

    @PostMapping("/register")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь успешно зарегистрирован. Возвращает токен доступа и токен обновления. " +
                                    "Дополнительно содержится информация о том, когда эти токены истекут и роль пользователя.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BearerLoginOut.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Пользователь с таким логином или почтой уже существует",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }

    )
    ResponseEntity<BearerLoginOut> register(final @RequestBody RegisterIn registerIn);

    @PostMapping("/refresh")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Токен доступа успешно обновлен. Возвращает новый токен доступа.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BearerLoginOut.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Токен доступа или токен обновления недействителен.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    ResponseEntity<?> refresh(final @RequestBody RefreshAccessTokenIn refreshAccessTokenIn);
}
