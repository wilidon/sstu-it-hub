package ru.sstu.studentprofile.domain.service.roleForProject;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sstu.studentprofile.data.models.project.RoleForProject;
import ru.sstu.studentprofile.data.repository.project.RoleForProjectRepository;
import ru.sstu.studentprofile.domain.exception.NotFoundException;
import ru.sstu.studentprofile.domain.service.roleForProject.dto.RoleForProjectOut;
import ru.sstu.studentprofile.domain.service.roleForProject.mappers.RoleForProjectMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleForProjectService {

    private RoleForProjectRepository roleForProjectRepository;
    private RoleForProjectMapper mapper;

    public RoleForProjectOut getRoleForProjectById(long roleForProjectId){
        RoleForProject role = roleForProjectRepository.findById(roleForProjectId)
                .orElseThrow(() -> new NotFoundException("Роль с id=%d не найдена".formatted(roleForProjectId)));

        return mapper.toRoleForProjectOut(role);
    }

    public List<RoleForProjectOut> getAll(){
        List<RoleForProject> roles = roleForProjectRepository.findAll();

        return mapper.toRoleForProjectOut(roles);
    }
}
