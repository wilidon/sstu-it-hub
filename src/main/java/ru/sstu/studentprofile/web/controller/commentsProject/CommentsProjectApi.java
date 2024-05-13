package ru.sstu.studentprofile.web.controller.commentsProject;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.commentsProject.dto.CommentProjectIn;
import ru.sstu.studentprofile.domain.service.event.dto.FilterStatusIn;

@Tag(name = "8. Комментарии к проекту")
@RequestMapping("/commentsProject")
@CrossOrigin(origins = "*")
public interface CommentsProjectApi {
    @GetMapping("/{projectId}")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Список комментариев проекта")
            }
    )
    ResponseEntity<?> getCommentsByProjectId(@RequestParam(value = "page", defaultValue = "1") int page,
                                             @RequestParam(value = "limit", defaultValue = "25") int limit,
                                             @PathVariable("projectId") long projectId);

    @PostMapping("/{projectId}")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Список мероприятий")
            }
    )
    ResponseEntity<?> create(@RequestBody CommentProjectIn commentProjectIn,
                             @PathVariable("projectId") long projectId, JwtAuthentication authentication);

    @DeleteMapping("/{commentId}")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Удаляет список мероприятий")
            }
    )
    ResponseEntity<?> delete(long commentId, JwtAuthentication authentication);
}
