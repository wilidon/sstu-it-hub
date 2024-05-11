package ru.sstu.studentprofile.web.controller.roleForProject.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sstu.studentprofile.domain.service.roleForProject.RoleForProjectService;
import ru.sstu.studentprofile.domain.service.roleForProject.dto.RoleForProjectOut;
import ru.sstu.studentprofile.web.controller.roleForProject.RoleForProjectApi;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/roleForProject")
@CrossOrigin("*")
public class RoleForProjectController implements RoleForProjectApi {
    private RoleForProjectService roleForProjectService;
    @Override
    public ResponseEntity<RoleForProjectOut> getRoleForProjectById(long roleForProjectId) {
        return ResponseEntity.ok(roleForProjectService.getRoleForProjectById(roleForProjectId));
    }

    @Override
    public ResponseEntity<List<RoleForProjectOut>> getAll() {
        return ResponseEntity.ok(roleForProjectService.getAll());
    }
}
