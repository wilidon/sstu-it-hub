package ru.sstu.studentprofile.domain.service.project;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.project.ProjectStatus;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.data.repository.project.ProjectRepository;
import ru.sstu.studentprofile.data.repository.user.UserRepository;
import ru.sstu.studentprofile.domain.exception.ForbiddenException;
import ru.sstu.studentprofile.domain.exception.NotFoundException;
import ru.sstu.studentprofile.domain.exception.UnprocessableEntityException;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.event.dto.EventStatusIn;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectIn;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectOut;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectStatusIn;
import ru.sstu.studentprofile.domain.service.storage.FileLoader;
import ru.sstu.studentprofile.domain.service.user.UserMapper;
import ru.sstu.studentprofile.domain.service.user.dto.UserOut;

import java.io.IOException;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserMapper mapperUser;
    private final ProjectMapper mapper;
    private final FileLoader fileLoader;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, UserMapper mapperUser, ProjectMapper mapper, @Qualifier("projectAvatarLoader") FileLoader fileLoader) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.mapperUser = mapperUser;
        this.mapper = mapper;
        this.fileLoader = fileLoader;
    }

    public ProjectOut findProjectById(final long id){
        Project project =  projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Проект не найден"));

        return mapper.toProjectOut(project);
    }

    public List<ProjectOut> all(int page){
        final short PAGE_SIZE = 25;
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE, Sort.by("createDate").descending());
        List<Project> projects = projectRepository.findAllByOrderByCreateDateDesc(pageable);

        return mapper.toProjectOut(projects);
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

    @Transactional
    public ProjectOut update(long projectId, ProjectIn projectIn, Authentication authentication){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Проект с id=%d не найдено".formatted(projectId)));
        long userId = ((JwtAuthentication) authentication).getUserId();

        if (project.getLeader().getId() != userId)
            throw new ForbiddenException("Вы не лидер проекта");

        project.setName(projectIn.name());
        project.setDescription(projectIn.description());
        project.setCreateDate(projectIn.createDate());
        project.setStatus(ProjectStatus.fromProjectStatusIn(projectIn.status()));

        projectRepository.save(project);
        return this.findProjectById(projectId);
    }

    @Transactional
    public ProjectOut updateProjectStatus(long projectId, ProjectStatusIn projectStatusIn, Authentication authentication){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Проект с id=%d не найдено".formatted(projectId)));
        long userId = ((JwtAuthentication) authentication).getUserId();

        if (project.getLeader().getId() != userId)
            throw new ForbiddenException("Вы не лидер проекта");

        project.setStatus(ProjectStatus.fromProjectStatusIn(projectStatusIn));

        projectRepository.save(project);
        return this.findProjectById(projectId);
    }

    @Transactional
    public ProjectOut uploadFileAvatar(long projectId, MultipartFile avatar, Authentication authentication) throws IOException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Проект с id=%d не найдено".formatted(projectId)));
        long userId = ((JwtAuthentication) authentication).getUserId();

        if (project.getLeader().getId() != userId)
            throw new ForbiddenException("Вы не лидер проекта");
        else if (avatar.isEmpty()) {
            throw new UnprocessableEntityException("Файл не загружен");
        }

        String filePath = fileLoader.load(avatar);
        project.setAvatar(filePath);
        projectRepository.save(project);
        return this.findProjectById(projectId);
    }

    @Transactional
    public ProjectOut deleteFileAvatar(long projectId, Authentication authentication){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Проект с id=%d не найдено".formatted(projectId)));
        long userId = ((JwtAuthentication) authentication).getUserId();

        if (project.getLeader().getId() != userId)
            throw new ForbiddenException("Вы не лидер проекта");

        project.setAvatar(null);
        projectRepository.save(project);
        return this.findProjectById(projectId);
    }

    public UserOut getProjectLeader(long projectId){
        Project project =  projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Проект не найден"));
        User leader = project.getLeader();

        return mapperUser.toUserOut(leader);
    }
}
