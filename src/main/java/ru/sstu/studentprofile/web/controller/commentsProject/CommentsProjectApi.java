package ru.sstu.studentprofile.web.controller.commentsProject;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.project.comment.dto.ProjectCommentIn;

@Tag(name = "8. Комментарии к проекту")
@RequestMapping("/commentsProject")
@CrossOrigin(origins = "*")
public interface CommentsProjectApi {
    @GetMapping("/{projectId}")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "УСТАРЕЛО. ИСПОЛЬЗУЙ /projects/{projectsId}/comments. \nСписок комментариев проекта")
            }
    )
    @Deprecated(forRemoval = true)
    ResponseEntity<?> getCommentsByProjectId(@RequestParam(value = "page", defaultValue = "1") int page,
                                             @RequestParam(value = "limit", defaultValue = "25") int limit,
                                             @PathVariable("projectId") long projectId);

    @PostMapping("/{projectId}")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "УСТАРЕЛО. ИСПОЛЬЗУЙ /projects/{projectsId}/comments. \nсоздает комментарий к проекту")
            }
    )
    @Deprecated(forRemoval = true)
    ResponseEntity<?> create(@RequestBody ProjectCommentIn projectCommentIn,
                             @PathVariable("projectId") long projectId, JwtAuthentication authentication);

    @DeleteMapping("/{commentId}")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "УСТАРЕЛО. ИСПОЛЬЗУЙ /projects/{projectsId}/comments. \nУдаляет комментарий проекта")
            }
    )
    @Deprecated(forRemoval = true)
    ResponseEntity<?> delete(long commentId, JwtAuthentication authentication);
}
