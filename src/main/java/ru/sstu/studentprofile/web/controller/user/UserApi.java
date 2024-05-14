package ru.sstu.studentprofile.web.controller.user;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.user.dto.UserAboutIn;
import ru.sstu.studentprofile.domain.service.user.dto.UserMediaIn;
import ru.sstu.studentprofile.domain.service.user.dto.UserOut;
import ru.sstu.studentprofile.domain.service.user.dto.UserRoleForProjectOut;
import ru.sstu.studentprofile.domain.service.user.dto.rating.UserRatingIn;
import ru.sstu.studentprofile.domain.service.util.PageableOut;

import java.io.IOException;
import java.util.List;

@Tag(name = "2. Пользователи")
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public interface UserApi {
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

    @GetMapping("/{userId}/events")
    @ApiResponses(
            value = @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает мероприятия пользователя",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageableOut.class)
                    )
            )
    )
    ResponseEntity<?> userEvents(@PathVariable("userId") long userId,
                                 @RequestParam(value = "page", defaultValue = "1") int size,
                                 @RequestParam(value = "limit", defaultValue = "25") int limit);

    @GetMapping("/{userId}/projects")
    @ApiResponses(
            value = @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает проекты пользователя, в которых он участвует",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageableOut.class)
                    )
            )
    )
    ResponseEntity<?> userProjects(@PathVariable("userId") long userId,
                                   @RequestParam(value = "page", defaultValue = "1") int size,
                                   @RequestParam(value = "limit", defaultValue = "25") int limit);

    @PatchMapping("/rolesForProject/{roleId}")
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<?> addRoleForProjects(@PathVariable("roleId") long roleId,
                                         JwtAuthentication authentication);

    @DeleteMapping("/rolesForProject/{roleId}")
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<?> deleteRoleForProjects(@PathVariable("roleId") long roleId,
                                            JwtAuthentication authentication);

    @GetMapping("/{userId}/reviews")
    @ApiResponses(
            value = @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает отзывы, которые имеются у этого пользователя",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageableOut.class)
                    )
            )
    )
    ResponseEntity<?> userReviews(@PathVariable("userId") long userId,
                                  @RequestParam(value = "page", defaultValue = "1") int size,
                                  @RequestParam(value = "limit", defaultValue = "25") int limit);

    @PatchMapping("/roleForProject/{userId}")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает измененные роли",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRoleForProjectOut.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Недостаточно прав",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ru.sstu.studentprofile.web.exp.ErrorMessage.class)
                    )

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
    ResponseEntity<List<UserRoleForProjectOut>> updateUserRoleForProjectById(@PathVariable long userId,
                                                                             @RequestBody @Valid List<UserRoleForProjectOut> roles, Authentication authentication);

    @PutMapping("/media")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = ""
                    )
            }
    )
    ResponseEntity<?> updateUserMedia(@RequestBody @Valid UserMediaIn mediaIn,
                                      JwtAuthentication authentication);

    @PutMapping("/about")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = ""
                    )
            }
    )
    ResponseEntity<?> updateUserAbout(@RequestBody @Valid UserAboutIn aboutIn,
                                      JwtAuthentication authentication);

    @PostMapping("/{userId}/rating")
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<?> addRating(@PathVariable long userId,
                                @RequestBody UserRatingIn userRatingIn,
                                JwtAuthentication authentication);

    @SecurityRequirement(name = "bearerAuth")
    @RequestMapping(value = "/background",
            method = RequestMethod.PATCH,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = @ApiResponse(
            responseCode = "200",
            description = "Обновляет фон пользователя.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserOut.class))
    )
    )
    ResponseEntity<UserOut> updateBackground(@RequestPart(name = "background") MultipartFile avatar, JwtAuthentication authentication) throws IOException;

    @SecurityRequirement(name = "bearerAuth")
    @RequestMapping(value = "/avatar",
            method = RequestMethod.PATCH,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = @ApiResponse(
            responseCode = "200",
            description = "Обновляет аватар пользователя.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserOut.class))
    )
    )
    ResponseEntity<UserOut> updateAvatar(@RequestPart(name = "avatar") MultipartFile avatar, JwtAuthentication authentication) throws IOException;

    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает профиль пользователя по id.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserOut.class))
            )
    })
    ResponseEntity<?> all(@RequestParam("page") int page, @RequestParam("limit") int limit, @RequestParam("roles") String roles);
}
