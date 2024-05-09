package ru.sstu.studentprofile.web.controller.project;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.domain.service.event.dto.EventIn;
import ru.sstu.studentprofile.domain.service.event.dto.EventOut;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectIn;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectOut;
import ru.sstu.studentprofile.domain.service.user.dto.UserOut;

@Tag(name = "4. Проекты")
@RequestMapping("/projects")
public interface ProjectApi {

    @SuppressWarnings("checkstyle:Indentation")
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
                                    schema = @Schema(implementation = ru.sstu.studentprofile.web.exp.ErrorMessage.class)
                            )
                    )
            }
    )
    ResponseEntity<?> create(@RequestBody @Valid ProjectIn projectIn,
                             Authentication authentication);

    @SuppressWarnings("checkstyle:Indentation")
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
}
