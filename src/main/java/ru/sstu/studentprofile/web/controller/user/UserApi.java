package ru.sstu.studentprofile.web.controller.user;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.user.dto.UserOut;

@Tag(name = "2. Пользователи")
@RequestMapping("/users")
public interface UserApi {

    @SuppressWarnings("checkstyle:Indentation")
    @GetMapping("/{userId}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает профиль пользователя по id.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserOut.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Профиль пользователя не найден",
                    content = @Content(
                            mediaType = "applicaiton/type",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    ResponseEntity<UserOut> getUserById(@PathVariable long userId);

    @SuppressWarnings("checkstyle:Indentation")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    @ApiResponses(value = @ApiResponse(
            responseCode = "200",
            description = "Возвращает пользователю его профиль.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserOut.class))
    )
    )
    ResponseEntity<UserOut> me(JwtAuthentication authentication);

    //два эндоинта для получаения лидера и получения мероприятия
}
