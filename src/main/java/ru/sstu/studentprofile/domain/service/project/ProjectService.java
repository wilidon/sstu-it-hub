package ru.sstu.studentprofile.domain.service.project;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.data.repository.project.ProjectRepository;
import ru.sstu.studentprofile.data.repository.user.UserRepository;
import ru.sstu.studentprofile.domain.exception.NotFoundException;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectIn;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectOut;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper mapper;
    
    public ProjectOut findProjectById(final long id){
        Project project =  projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Проект не найден"));

        return mapper.toProjectOut(project);
    }

    @Transactional
    public ProjectOut create(ProjectIn projectIn, Authentication authentication){
        long userId = ((JwtAuthentication) authentication).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(userId)));

        Project project = mapper.toProject(projectIn, user);
        projectRepository.save(project);

        return mapper.toProjectOut(project);
    }
}
