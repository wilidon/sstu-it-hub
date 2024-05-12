package ru.sstu.studentprofile.web.controller.request;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.sstu.studentprofile.domain.service.request.dto.RequestIn;
import ru.sstu.studentprofile.domain.service.request.dto.RequestOut;
import ru.sstu.studentprofile.domain.service.request.dto.RequestResultIn;
import ru.sstu.studentprofile.domain.service.roleForProject.dto.RoleForProjectOut;

import java.util.List;

@Tag(name = "6. Заявки")
@RequestMapping("/request")
@CrossOrigin(origins = "*")
public interface RequestApi {
    @PostMapping("")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Создает заявку",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RequestOut.class))
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
    ResponseEntity<RequestOut> create(@RequestBody RequestIn requestIn, Authentication authentication);

    @PatchMapping("/{requestId}")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ответ на заявку",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RequestOut.class))
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
    ResponseEntity<RequestOut> patchRequest(@PathVariable long requestId, @RequestParam RequestResultIn requestResultIn, Authentication authentication);

    @GetMapping("/all")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Получение заявок пользователя",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RequestOut.class))
            ),
    })
    ResponseEntity<List<RequestOut>> all(Authentication authentication);
}
