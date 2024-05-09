package ru.sstu.studentprofile.web.controller.project.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.domain.service.project.ProjectService;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectIn;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectOut;
import ru.sstu.studentprofile.domain.service.user.dto.UserOut;
import ru.sstu.studentprofile.web.controller.project.ProjectApi;

@RestController
@AllArgsConstructor
@RequestMapping("/projects")
public class ProjectController implements ProjectApi {
    private final ProjectService projectService;

    @Override
    public ResponseEntity<?> create(ProjectIn projectIn, Authentication authentication){
        return ResponseEntity.ok(projectService.create(projectIn, authentication));
    }

    @Override
    public ResponseEntity<ProjectOut> getProjectById(final long projectId) {
        return ResponseEntity.ok(projectService.findProjectById(projectId));
    }
}
