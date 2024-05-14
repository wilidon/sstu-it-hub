package ru.sstu.studentprofile.web.controller.roleForProject;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sstu.studentprofile.domain.service.roleForProject.dto.RoleForProjectOut;
import ru.sstu.studentprofile.domain.service.user.dto.UserOut;

import java.util.List;

@Tag(name = "5. Роли для проектов")
@RequestMapping("/roleForProject")
@CrossOrigin(origins = "*")
public interface RoleForProjectApi {
    @GetMapping("{roleForProjectId}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает сущность роли",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RoleForProjectOut.class))
            ),
    })
    ResponseEntity<RoleForProjectOut> getRoleForProjectById(@PathVariable("roleForProjectId") long roleForProjectId);

    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Возвращает все роли",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RoleForProjectOut.class))
            ),
    })
    ResponseEntity<List<RoleForProjectOut>> getAll();
}
