package ru.sstu.studentprofile.web.controller.project;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sstu.studentprofile.domain.service.event.dto.EventOut;
import ru.sstu.studentprofile.domain.service.event.dto.ShortEventOut;
import ru.sstu.studentprofile.domain.service.project.dto.*;
import ru.sstu.studentprofile.domain.service.user.dto.UserOut;
import ru.sstu.studentprofile.domain.service.util.PageableOut;

import java.io.IOException;
import java.util.List;

@Tag(name = "4. Проекты")
@RequestMapping("/projects")
@CrossOrigin(origins = "*")
public interface ProjectApi {
    @PostMapping()
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201",
                            description = "Проект создан",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProjectOut.class))),
                    @ApiResponse(responseCode = "422",
                            description = "Неверные данные",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    ResponseEntity<?> create(@RequestBody @Valid ProjectIn projectIn,
                             Authentication authentication);

    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает все проекты"
            ),
    })
    ResponseEntity<?> getAllProjects(@RequestParam(value = "search", defaultValue = "") String search,
                                     @RequestParam(value = "needActualRoles", defaultValue = "false") boolean needActualRoles,
                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "limit", defaultValue = "25") int limit,
                                     @RequestParam(value = "status") ProjectStatusSearchIn status);

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{projectId}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает проект по id.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectOut.class))
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
    ResponseEntity<ProjectOut> getProjectById(@PathVariable long projectId);

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{projectId}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Изменяет проект по id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectOut.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Мероприятие не найдено",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ru.sstu.studentprofile.web.exp.ErrorMessage.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Неверные данные",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ru.sstu.studentprofile.web.exp.ErrorMessage.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Недостаточно прав",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ru.sstu.studentprofile.web.exp.ErrorMessage.class)
                    )

            )
    })
    ResponseEntity<ProjectOut> updateProjectById(@PathVariable long projectId, @RequestBody @Valid ProjectIn projectIn, Authentication authentication);

    @PatchMapping("/{projectId}/status")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Обновляет статус проекта. Возвращает обновленный проект.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectOut.class))
            ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Проект не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ru.sstu.studentprofile.web.exp.ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Неверные данные",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ru.sstu.studentprofile.web.exp.ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ru.sstu.studentprofile.web.exp.ErrorMessage.class)
                            )

                    )}
    )
    ResponseEntity<ProjectOut> updateProjectStatus(@PathVariable("projectId") long projectId,
                                                   ProjectStatusIn projectStatusIn,
                                                   Authentication authentication);

    @RequestMapping(value = "/{projectId}/avatar",
            method = RequestMethod.PATCH,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            value = @ApiResponse(
                    responseCode = "200",
                    description = "Устанавливает аватар проекта. Возвращает проект с новым аватаром.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectOut.class))
            )
    )
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<ProjectOut> uploadFileAvatar(
            @PathVariable("projectId") long projectId,
            @RequestPart(name = "avatar") MultipartFile avatar, @NotNull Authentication authentication) throws IOException;

    @DeleteMapping("/{projectId}/avatar")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = @ApiResponse(
                    responseCode = "200",
                    description = "Удаляет аватар проекта. " +
                            "Возвращает проект с удаленным аватаром.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectOut.class))
            )
    )
    ResponseEntity<ProjectOut> deleteAvatar(
            @PathVariable("projectId") long projectId,
            @NotNull Authentication authentication);

    @PatchMapping("/{projectId}/event")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Обновление мероприятия проекта. Возвращает модель проекта",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectEventOut.class))
            ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Проект не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ru.sstu.studentprofile.web.exp.ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Неверные данные",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ru.sstu.studentprofile.web.exp.ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ru.sstu.studentprofile.web.exp.ErrorMessage.class)
                            )

                    )}
    )
    ResponseEntity<ProjectEventOut> updateProjectEvent(@PathVariable("projectId") long projectId,
                                                       long eventId,
                                                       Authentication authentication);

    @DeleteMapping("/{projectId}/event")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Удаление мероприятия проекта. Возвращает модель проекта",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectOut.class))
            ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Проект не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ru.sstu.studentprofile.web.exp.ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Неверные данные",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ru.sstu.studentprofile.web.exp.ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ru.sstu.studentprofile.web.exp.ErrorMessage.class)
                            )

                    )}
    )
    ResponseEntity<ProjectOut> deleteProjectEvent(@PathVariable("projectId") long projectId, Authentication authentication);

    @PatchMapping("/{projectId}/actualRoles")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Изменение актуальный ролей проекта. Возвращает модель проекта",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectOut.class))
            ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Проект не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ru.sstu.studentprofile.web.exp.ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Неверные данные",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ru.sstu.studentprofile.web.exp.ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ru.sstu.studentprofile.web.exp.ErrorMessage.class)
                            )

                    )}
    )
    ResponseEntity<List<ProjectActualRoleOut>> updateProjectActualRole(@RequestBody @Valid List<ProjectActualRoleOut> roles, long projectId, Authentication authentication);
}
