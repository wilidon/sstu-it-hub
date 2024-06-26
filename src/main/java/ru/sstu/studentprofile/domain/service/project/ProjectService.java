package ru.sstu.studentprofile.domain.service.project;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sstu.studentprofile.data.models.event.Event;
import ru.sstu.studentprofile.data.models.project.*;
import ru.sstu.studentprofile.data.repository.project.ActualRoleForProjectRepository;
import ru.sstu.studentprofile.data.repository.project.ProjectMemberRepository;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.data.repository.event.EventRepository;
import ru.sstu.studentprofile.data.repository.project.ProjectRepository;
import ru.sstu.studentprofile.data.repository.project.RoleForProjectRepository;
import ru.sstu.studentprofile.data.repository.user.UserRepository;
import ru.sstu.studentprofile.domain.exception.ForbiddenException;
import ru.sstu.studentprofile.domain.exception.NotFoundException;
import ru.sstu.studentprofile.domain.exception.UnprocessableEntityException;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.project.dto.*;
import ru.sstu.studentprofile.domain.service.project.mappers.ProjectActualRoleMapper;
import ru.sstu.studentprofile.domain.service.project.mappers.ProjectEventMapper;
import ru.sstu.studentprofile.domain.service.project.mappers.ProjectMapper;
import ru.sstu.studentprofile.domain.service.project.mappers.ProjectMemberMapper;
import ru.sstu.studentprofile.domain.service.roleForProject.dto.RoleForProjectOut;
import ru.sstu.studentprofile.domain.service.storage.FileLoader;
import ru.sstu.studentprofile.domain.service.util.PageableOut;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ProjectMemberMapper mapperProjectMember;
    private final ProjectMapper mapper;
    private final FileLoader fileLoader;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectActualRoleMapper mapperActualRoleMapper;
    private final ProjectEventMapper mapperProjectEvent;
    private final ActualRoleForProjectRepository actualRoleForProjectRepository;
    private final RoleForProjectRepository roleForProjectRepository;


    @Autowired
    public ProjectService(ProjectRepository projectRepository,
                          UserRepository userRepository,
                          EventRepository eventRepository,
                          ProjectMapper mapper,
                          @Qualifier("projectAvatarLoader") FileLoader fileLoader,
                          ProjectMemberMapper mapperProjectMember,
                          ProjectMemberRepository projectMemberRepository,
                          ProjectActualRoleMapper mapperActualRoleMapper,
                          ProjectEventMapper mapperProjectEvent,
                          ActualRoleForProjectRepository actualRoleForProjectRepository,
                          RoleForProjectRepository roleForProjectRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.mapper = mapper;
        this.fileLoader = fileLoader;
        this.mapperProjectMember = mapperProjectMember;
        this.projectMemberRepository = projectMemberRepository;
        this.mapperActualRoleMapper = mapperActualRoleMapper;
        this.mapperProjectEvent = mapperProjectEvent;
        this.actualRoleForProjectRepository = actualRoleForProjectRepository;
        this.roleForProjectRepository = roleForProjectRepository;
    }

    @Transactional
    public ProjectOut create(ProjectIn projectIn, Authentication authentication) {
        long userId = ((JwtAuthentication) authentication).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(userId)));

        ProjectMedia projectMedia = new ProjectMedia();
        Project project = mapper.toProject(projectIn, user);
        project.setProjectMedia(projectMedia);
        project.setCreateDate(LocalDateTime.now());
        project.setStatus(ProjectStatus.PLANNED);
        projectRepository.save(project);

        ProjectMember leader = new ProjectMember();
        leader.setProject(project);
        leader.setUser(user);

        projectMemberRepository.save(leader);

        return mapper.toProjectOut(project, mapperProjectMember, mapperActualRoleMapper, mapperProjectEvent);
    }

    public ProjectOut findProjectById(final long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Проект не найден"));

        return mapper.toProjectOut(project, mapperProjectMember, mapperActualRoleMapper, mapperProjectEvent);
    }

    public PageableOut<ProjectOut> all(String query, boolean needActualRoles, int page, int limit, ProjectStatusSearchIn status) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Project> projects;
        if (needActualRoles) {
            projects = projectRepository.findAllByActualRoleProject(pageable, status==ProjectStatusSearchIn.ALL?null:ProjectStatus.fromProjectStatusSearchIn(status));
        }
        else if (query.isEmpty()) {
            projects = projectRepository.findAllByOrderByCreateDateDesc(pageable, status==ProjectStatusSearchIn.ALL?null:ProjectStatus.fromProjectStatusSearchIn(status));
        }
        else {
            projects = projectRepository.findAllByQuery(query.toLowerCase(), pageable, status==ProjectStatusSearchIn.ALL?null:ProjectStatus.fromProjectStatusSearchIn(status));
        }

        List<ProjectOut> projectsOut = new ArrayList<>();
        for (Project project : projects.getContent()) {
            ProjectOut projectOut = mapper.toProjectOut(project, mapperProjectMember, mapperActualRoleMapper, mapperProjectEvent);
            projectsOut.add(projectOut);
        }

        return new PageableOut<>(
                page,
                projects.getSize(),
                projects.getTotalPages(),
                projects.getTotalElements(),
                projectsOut
        );
    }

    public void updateMedia() {

    }

    @Transactional
    public ProjectOut update(long projectId, ProjectIn projectIn, Authentication authentication) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Проект с id=%d не найдено".formatted(projectId)));
        long userId = ((JwtAuthentication) authentication).getUserId();

        if (project.getLeader().getId() != userId)
            throw new ForbiddenException("Вы не лидер проекта");

        project.setName(projectIn.name());
        project.setDescription(projectIn.description());
        project.getProjectMedia().setGithubLink(projectIn.githubUrl());
        project.getProjectMedia().setUrlSite(projectIn.siteUrl());
        project.getProjectMedia().setStack(projectIn.stack());

        projectRepository.save(project);
        return this.findProjectById(projectId);
    }

    @Transactional
    public ProjectOut updateProjectStatus(long projectId, ProjectStatusIn projectStatusIn, Authentication authentication) {
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
    public ProjectOut deleteFileAvatar(long projectId, Authentication authentication) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Проект с id=%d не найдено".formatted(projectId)));
        long userId = ((JwtAuthentication) authentication).getUserId();

        if (project.getLeader().getId() != userId)
            throw new ForbiddenException("Вы не лидер проекта");

        project.setAvatar(null);
        projectRepository.save(project);
        return this.findProjectById(projectId);
    }

    @Transactional
    public ProjectEventOut updateProjectEvent(long projectId, long eventId, Authentication authentication) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Проект с id=%d не найдено".formatted(projectId)));
        long userId = ((JwtAuthentication) authentication).getUserId();

        if (project.getLeader().getId() != userId)
            throw new ForbiddenException("Вы не лидер проекта");

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Мероприятие с id=%d не найдено".formatted(eventId)));

        project.setEvent(event);
        projectRepository.save(project);

        return mapperProjectEvent.toProjectEventOut(event);
    }

    @Transactional
    public ProjectOut deleteProjectEvent(long projectId, Authentication authentication) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Проект с id=%d не найдено".formatted(projectId)));
        long userId = ((JwtAuthentication) authentication).getUserId();

        if (project.getLeader().getId() != userId)
            throw new ForbiddenException("Вы не лидер проекта");

        project.setEvent(null);
        projectRepository.save(project);

        return mapper.toProjectOut(project, mapperProjectMember, mapperActualRoleMapper, mapperProjectEvent);
    }

    @Transactional
    public List<ProjectActualRoleOut> updateProjectActualRole(List<ProjectActualRoleOut> roles, long projectId, Authentication authentication) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Проект с id=%d не найдено".formatted(projectId)));
        long userId = ((JwtAuthentication) authentication).getUserId();

        if (project.getLeader().getId() != userId)
            throw new ForbiddenException("Вы не лидер проекта");

        actualRoleForProjectRepository.deleteByIdProject(projectId);

        for (ProjectActualRoleOut roleActual : roles) {
            RoleForProject roleForProject = roleForProjectRepository.findById(roleActual.id())
                    .orElseThrow(() -> new NotFoundException("Роль для проекта с id=%d не найдено".formatted(roleActual.id())));
            ;

            ActualRoleForProject roleActualNew = new ActualRoleForProject();
            roleActualNew.setProject(project);
            roleActualNew.setRole(roleForProject);

            actualRoleForProjectRepository.save(roleActualNew);
        }

        return roles;
    }

    @Transactional
    public ProjectOut deleteProjectMember(long projectId, long memberId, JwtAuthentication authentication){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Проект с id=%d не найдено".formatted(projectId)));
        long userId = authentication.getUserId();

        if (project.getLeader().getId() != userId)
            throw new ForbiddenException("Вы не лидер проекта");

        projectMemberRepository.deleteById(memberId);

        return this.findProjectById(projectId);
    }

    public PageableOut<ProjectOut> getAllNeeded(int page, int limit, JwtAuthentication authentication){
        if (authentication == null){
            Pageable pageable = PageRequest.of(page - 1, limit);
            Page<Project> projects = projectRepository.findAll(pageable);

            List<ProjectOut> projectsOut = new ArrayList<>();
            for (Project project : projects.getContent()) {
                projectsOut.add(mapper.toProjectOut(project, mapperProjectMember, mapperActualRoleMapper, mapperProjectEvent));
            }

            return new PageableOut<>(
                    page,
                    projects.getSize(),
                    projects.getTotalPages(),
                    projects.getTotalElements(),
                    projectsOut
            );
        }
        else{
            long userId = authentication.getUserId();

            Pageable pageable = PageRequest.of(page - 1, limit);
            Page<Project> projects = projectRepository.findAllByRoleForProject(pageable, userId);

            List<ProjectOut> projectsOut = new ArrayList<>();
            for (Project project : projects.getContent()) {
                projectsOut.add(mapper.toProjectOut(project, mapperProjectMember, mapperActualRoleMapper, mapperProjectEvent));
            }

            if (projects.getTotalElements() == 0){
                return this.all("", true, page, limit, ProjectStatusSearchIn.ALL);
            }

            return new PageableOut<>(
                    page,
                    projects.getSize(),
                    projects.getTotalPages(),
                    projects.getTotalElements(),
                    projectsOut
            );
        }
    }
}
