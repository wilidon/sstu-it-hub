package ru.sstu.studentprofile.web.controller.project;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.project.comment.dto.ProjectCommentIn;
import ru.sstu.studentprofile.domain.service.project.comment.dto.ProjectCommentUpdateIn;

@Tag(name = "4. Проекты")
@RequestMapping("/projects")
@CrossOrigin(origins = "*")
public interface ProjectCommentsApi {
    @GetMapping("/{projectId}/comments")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Список комментариев проекта")
            }
    )
    ResponseEntity<?> getAll(@PathVariable(name = "projectId") long projectId,
                             @RequestParam(value = "page", defaultValue = "1") int size,
                             @RequestParam(value = "limit", defaultValue = "25") int limit);

    @PostMapping("/{projectId}/comments")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Отправляет комментарий на страницу проекта")
            }
    )
    ResponseEntity<?> add(@RequestBody ProjectCommentIn projectCommentIn,
                          @PathVariable(name = "projectId") long projectId,
                          JwtAuthentication authentication);

    @PatchMapping("/{projectId}/comments/{commentId}")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Обновляет комментарий на странице проекта")
            }
    )
    ResponseEntity<?> update(@RequestBody ProjectCommentUpdateIn projectCommentUpdateIn,
                             @PathVariable(name = "projectId") long projectId,
                             @PathVariable(name = "commentId") long commentId,
                             JwtAuthentication authentication);

    @DeleteMapping("/{projectId}/comments/{commentId}")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Удаляет комментарий со страницы проекта")
            }
    )
    ResponseEntity<?> delete(long commentId, JwtAuthentication authentication);
}
