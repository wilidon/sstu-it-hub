package ru.sstu.studentprofile.web.controller.project.impl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.project.ProjectMember;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.event.dto.EventOut;
import ru.sstu.studentprofile.domain.service.event.dto.EventStatusIn;
import ru.sstu.studentprofile.domain.service.event.dto.ShortEventOut;
import ru.sstu.studentprofile.domain.service.project.ProjectService;
import ru.sstu.studentprofile.domain.service.project.dto.*;
import ru.sstu.studentprofile.domain.service.user.dto.UserOut;
import ru.sstu.studentprofile.domain.service.util.PageableOut;
import ru.sstu.studentprofile.web.controller.project.ProjectApi;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/projects")
@CrossOrigin("*")
public class ProjectController implements ProjectApi {
    private final ProjectService projectService;

    @Override
    public ResponseEntity<?> create(ProjectIn projectIn, Authentication authentication){
        return ResponseEntity.ok(projectService.create(projectIn, authentication));
    }

    @Override
    public ResponseEntity<ProjectOut> updateProjectById(long projectId, ProjectIn projectIn, Authentication authentication){
        return ResponseEntity.ok(projectService.update(projectId, projectIn, authentication));
    }

    @Override
    public ResponseEntity<PageableOut<ProjectOut>> getAllProjects(String search, boolean needActualRoles, int page, int limit, ProjectStatusSearchIn status){
        return ResponseEntity.ok(projectService.all(search, needActualRoles, page, limit, status));
    }

    @Override
    public ResponseEntity<ProjectOut> getProjectById(final long projectId) {
        return ResponseEntity.ok(projectService.findProjectById(projectId));
    }

    @Override
    public ResponseEntity<ProjectOut> updateProjectStatus(long projectId, ProjectStatusIn projectStatusIn, Authentication authentication){
        return ResponseEntity.ok(projectService.updateProjectStatus(projectId, projectStatusIn, authentication));
    }

    @Override
    public ResponseEntity<ProjectOut> uploadFileAvatar(long projectId, MultipartFile avatar, Authentication authentication) throws IOException {
        return ResponseEntity.ok(projectService.uploadFileAvatar(projectId, avatar, authentication));
    }

    @Override
    public ResponseEntity<ProjectOut> deleteAvatar(long projectId, Authentication authentication) {
        return ResponseEntity.ok(projectService.deleteFileAvatar(projectId, authentication));
    }

    @Override
    public ResponseEntity<ProjectEventOut> updateProjectEvent(long projectId, long eventId, Authentication authentication) {
        return ResponseEntity.ok(projectService.updateProjectEvent(projectId, eventId, authentication));
    }

    @Override
    public ResponseEntity<ProjectOut> deleteProjectEvent(long projectId, Authentication authentication) {
        return ResponseEntity.ok(projectService.deleteProjectEvent(projectId, authentication));
    }

    @Override
    public ResponseEntity<List<ProjectActualRoleOut>> updateProjectActualRole(List<ProjectActualRoleOut> roles, long projectId, Authentication authentication) {
        return ResponseEntity.ok(projectService.updateProjectActualRole(roles, projectId, authentication));
    }

    @Override
    public ResponseEntity<ProjectOut> deleteProjectMember(long projectId, long memberId, JwtAuthentication authentication) {
        return ResponseEntity.ok(projectService.deleteProjectMember(projectId, memberId, authentication));
    }

    @Override
    public ResponseEntity<?> getAllProjectsNeeded(int page, int limit, JwtAuthentication authentication) {
        return ResponseEntity.ok(projectService.getAllNeeded(page, limit, authentication));
    }
}

// TODO ДОБАВИТЬ ФУНКЦИЮ ПО УДАЛЕНИЮ ПРИВЯЗКИ МЕРОПРИЯТИЯ К ПРОЕКТУ
