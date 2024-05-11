package ru.sstu.studentprofile.web.controller.event;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sstu.studentprofile.domain.service.event.dto.EventIn;
import ru.sstu.studentprofile.domain.service.event.dto.EventOut;
import ru.sstu.studentprofile.domain.service.event.dto.EventStatusIn;
import ru.sstu.studentprofile.domain.service.event.dto.FilterStatusIn;
import ru.sstu.studentprofile.web.exp.ErrorMessage;

import java.io.IOException;

@Tag(name = "3. Мероприятия")
@RequestMapping("/events")
@CrossOrigin("*")
public interface EventApi {

    @GetMapping("/all")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Список мероприятий")
            }
    )
    ResponseEntity<?> getAllEvents(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "limit", defaultValue = "25") int limit,
                                   @RequestParam(value = "status") FilterStatusIn eventStatusIn);

    @GetMapping("/{id}")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "Мероприятие найдено",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EventOut.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Мероприятие не найдено",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EventOut.class)
                            )
                    )
            }
    )
    ResponseEntity<?> getEventById(@PathVariable long id);

    @PostMapping()
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201",
                            description = "Мероприятие создано",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EventOut.class))),
                    @ApiResponse(responseCode = "422",
                            description = "Неверные данные",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    ResponseEntity<?> create(@RequestBody @Valid EventIn eventIn,
                             Authentication authentication);

    @PutMapping("/{eventId}")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Обновляет мероприятие. Возвращает обновленное мероприятие.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EventOut.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Мероприятие не найдено",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Неверные данные",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Недостаточно прав",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )

            )}
    )
    ResponseEntity<?> updateEvent(@PathVariable("eventId") long eventId,
                                        @RequestBody @Valid EventIn eventIn,
                                        Authentication authentication);

    @PatchMapping("/{eventId}/status")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Обновляет статус мероприятие. Возвращает обновленное мероприятие.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EventOut.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Мероприятие не найдено",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Неверные данные",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Недостаточно прав",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )

            )}
    )
    ResponseEntity<?> updateEventStatus(@PathVariable("eventId") long eventId,
                                        EventStatusIn eventStatusIn,
                                        Authentication authentication);

    @RequestMapping(value = "/{eventId}/avatar",
            method = RequestMethod.PATCH,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiResponses(
            value = @ApiResponse(
                    responseCode = "200",
                    description = "Устанавливает аватар мероприятия. Возвращает мероприятие с новым аватаром.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EventOut.class))
            )
    )
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<EventOut> uploadFile(
            @PathVariable("eventId") long eventId,
            @NotNull Authentication authentication,
            @RequestPart(name = "avatar") MultipartFile avatar) throws IOException;

    @DeleteMapping("/{eventId}/avatar")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = @ApiResponse(
                    responseCode = "200",
                    description = "Удаляет аватар мероприятия. " +
                            "Возвращает мероприятие с удаленным аватаром.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EventOut.class))
            )
    )
    ResponseEntity<EventOut> deleteAvatar(
            @PathVariable("eventId") long eventId,
            @NotNull Authentication authentication);
}
