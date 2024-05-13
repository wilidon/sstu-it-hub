package ru.sstu.studentprofile.domain.service.user;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.project.RoleForProject;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.data.models.user.UserRoleForProject;
import ru.sstu.studentprofile.data.repository.event.EventRepository;
import ru.sstu.studentprofile.data.repository.project.ProjectRepository;
import ru.sstu.studentprofile.data.repository.project.RoleForProjectRepository;
import ru.sstu.studentprofile.data.repository.user.UserRepository;
import ru.sstu.studentprofile.data.repository.user.UserRoleForProjectRepository;
import ru.sstu.studentprofile.domain.exception.ForbiddenException;
import ru.sstu.studentprofile.domain.exception.NotFoundException;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.security.UserDetailsImpl;
import ru.sstu.studentprofile.domain.service.storage.UserAvatarLoader;
import ru.sstu.studentprofile.domain.service.storage.UserBackgroundLoader;
import ru.sstu.studentprofile.domain.service.user.dto.UserEvent;
import ru.sstu.studentprofile.domain.service.user.dto.UserOut;
import ru.sstu.studentprofile.domain.service.user.dto.UserProject;
import ru.sstu.studentprofile.domain.service.user.dto.UserRoleForProjectOut;
import ru.sstu.studentprofile.domain.service.util.PageableOut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserRoleForProjectRepository userRoleForProjectRepository;
    private final RoleForProjectRepository roleForProjectRepository;
    private final EventRepository eventRepository;
    private final ProjectRepository projectRepository;
    private final UserAvatarLoader userAvatarLoader;
    private final UserBackgroundLoader userBackgroundLoader;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       UserRoleForProjectRepository userRoleForProjectRepository,
                       RoleForProjectRepository roleForProjectRepository, EventRepository eventRepository, ProjectRepository projectRepository,
                       @Qualifier("userAvatarLoader") UserAvatarLoader userAvatarLoader,
                       @Qualifier("userBackgroundLoader") UserBackgroundLoader userBackgroundLoader) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userRoleForProjectRepository = userRoleForProjectRepository;
        this.roleForProjectRepository = roleForProjectRepository;
        this.eventRepository = eventRepository;
        this.projectRepository = projectRepository;
        this.userAvatarLoader = userAvatarLoader;
        this.userBackgroundLoader = userBackgroundLoader;
    }

    public UserOut findUserById(final long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

       return userMapper.toUserOut(user);
    }


    public UserOut findMe(final @NotNull JwtAuthentication authentication) {
        final User user = ((UserDetailsImpl) authentication.getPrincipal()).user();

        return findUserById(user.getId());
    }

    public PageableOut<UserEvent> findAllUserEvents(long userId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1,
                limit,
                Sort.by("id").descending());
        Page<Event> events = eventRepository.findEventByUserId(userId,
                pageable);

        List<UserEvent> userEvents = new ArrayList<>();
        for (Event event : events.getContent()) {
            userEvents.add(userMapper.toUserEvent(event, 100L));
        }

        return new PageableOut<>(page,
                events.getSize(),
                events.getTotalPages(),
                events.getNumberOfElements(),
                userEvents);
    }

    public PageableOut<UserProject> findAllUserProjects(long userId, int page, int limit) {
        Pageable pageable = PageRequest.of(page-1,
                limit,
                Sort.by("id").descending());

        Page<Project> projects = projectRepository.findAllProjectsByUserId(
                userId, pageable
        );

        List<UserProject> userProject = new ArrayList<>();
        for (Project project : projects.getContent()) {
            userProject.add(userMapper.toUserProject(project));
        }

        return new PageableOut<>(
                page,
                projects.getSize(),
                projects.getTotalPages(),
                projects.getNumberOfElements(),
                userProject
        );
    }


    @Transactional
    public List<UserRoleForProjectOut> updateUserRoleForProjectById(long userId, List<UserRoleForProjectOut> roles, Authentication authentication){
        long userIdOwner = ((JwtAuthentication) authentication).getUserId();
        if (userId != userIdOwner)
            throw new ForbiddenException("Вы не лидер проекта");

        final User user = userRepository.findById(userIdOwner)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        userRoleForProjectRepository.deleteByUserId(userIdOwner);

        for (UserRoleForProjectOut role : roles){
            RoleForProject roleSource = roleForProjectRepository.findById(role.id())
                    .orElseThrow(() -> new NotFoundException("Роль не найдена"));

            UserRoleForProject roleNew = new UserRoleForProject();
            roleNew.setUser(user);
            roleNew.setRole(roleSource);

            userRoleForProjectRepository.save(roleNew);
        }

        return roles;
    }

    @Transactional
    public UserOut updateAvatar(MultipartFile avatar, JwtAuthentication authentication) throws IOException {
        long userId = authentication.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        String filePath = userAvatarLoader.load(avatar);
        user.setAvatar(filePath);
        userRepository.save(user);

        return userMapper.toUserOut(user);
    }

    @Transactional
    public UserOut updateBackground(MultipartFile background, JwtAuthentication authentication) throws IOException {
        long userId = authentication.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        String filePath = userAvatarLoader.load(background);
        user.setBackground(filePath);
        userRepository.save(user);

        return userMapper.toUserOut(user);
    }
}
