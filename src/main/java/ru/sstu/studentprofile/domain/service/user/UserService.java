package ru.sstu.studentprofile.domain.service.user;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
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
import ru.sstu.studentprofile.data.models.user.UserRating;
import ru.sstu.studentprofile.data.models.user.UserRatingType;
import ru.sstu.studentprofile.data.models.user.UserReview;
import ru.sstu.studentprofile.data.models.user.UserRoleForProject;
import ru.sstu.studentprofile.data.repository.event.EventRepository;
import ru.sstu.studentprofile.data.repository.project.ProjectMemberRepository;
import ru.sstu.studentprofile.data.repository.project.ProjectRepository;
import ru.sstu.studentprofile.data.repository.project.RoleForProjectRepository;
import ru.sstu.studentprofile.data.repository.user.UserRatingRepository;
import ru.sstu.studentprofile.data.repository.user.UserRepository;
import ru.sstu.studentprofile.data.repository.user.UserReviewRepository;
import ru.sstu.studentprofile.data.repository.user.UserRoleForProjectRepository;
import ru.sstu.studentprofile.data.repository.user.projection.UserRatingProjection;
import ru.sstu.studentprofile.domain.exception.ConflictException;
import ru.sstu.studentprofile.domain.exception.ForbiddenException;
import ru.sstu.studentprofile.domain.exception.NotFoundException;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.security.UserDetailsImpl;
import ru.sstu.studentprofile.domain.service.storage.UserAvatarLoader;
import ru.sstu.studentprofile.domain.service.storage.UserBackgroundLoader;
import ru.sstu.studentprofile.domain.service.user.dto.UserAboutIn;
import ru.sstu.studentprofile.domain.service.user.dto.UserMediaIn;
import ru.sstu.studentprofile.domain.service.user.dto.UserEvent;
import ru.sstu.studentprofile.domain.service.user.dto.UserOut;
import ru.sstu.studentprofile.domain.service.user.dto.UserProject;
import ru.sstu.studentprofile.domain.service.user.dto.UserReviewOut;
import ru.sstu.studentprofile.domain.service.user.dto.UserRoleForProjectOut;
import ru.sstu.studentprofile.domain.service.user.dto.rating.UserRatingIn;
import ru.sstu.studentprofile.domain.service.user.dto.rating.UserRatingOut;
import ru.sstu.studentprofile.domain.service.util.PageableOut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final UserReviewRepository userReviewRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRatingRepository userRatingRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       UserRoleForProjectRepository userRoleForProjectRepository,
                       RoleForProjectRepository roleForProjectRepository, EventRepository eventRepository, ProjectRepository projectRepository,
                       @Qualifier("userAvatarLoader") UserAvatarLoader userAvatarLoader,
                       @Qualifier("userBackgroundLoader") UserBackgroundLoader userBackgroundLoader, UserReviewRepository userReviewRepository, ProjectMemberRepository projectMemberRepository, UserRatingRepository userRatingRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userRoleForProjectRepository = userRoleForProjectRepository;
        this.roleForProjectRepository = roleForProjectRepository;
        this.eventRepository = eventRepository;
        this.projectRepository = projectRepository;
        this.userAvatarLoader = userAvatarLoader;
        this.userBackgroundLoader = userBackgroundLoader;
        this.userReviewRepository = userReviewRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.userRatingRepository = userRatingRepository;
    }

    public UserOut findUserById(final long id) {
        final int REVIEW_LIMIT = 3;
        final int REVIEW_PAGE = 1;

        final User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Pageable pageable = PageRequest.of(REVIEW_PAGE - 1,
                REVIEW_LIMIT,
                Sort.by("createdAt").descending());
        final Page<UserReview> reviews = userReviewRepository.findAllByRecipientId(id,
                pageable);

        final List<UserRatingProjection> userRating = userRatingRepository.findUserRatingProjectionUsingRecipientId(id);

        return userMapper.toUserOut(user, reviews.getContent(), getUserRatingOut(userRating));
    }


    public UserOut findMe(final @NotNull JwtAuthentication authentication) {
        final User user = ((UserDetailsImpl) authentication.getPrincipal()).user();

        return findUserById(user.getId());
    }

    /**
     * Метод, который потом нужно переписать в более нормальную форму.
     * Сейчас здесь захардкожено
     */
    private UserRatingOut getUserRatingOut(List<UserRatingProjection> userRatingOut) {
        long politeness = 0;
        long learningAbility = 0;
        long responsibility = 0;
        long creativity = 0;

        for (UserRatingProjection userRatingProjection : userRatingOut) {
            if (userRatingProjection.getRatingType().equals(UserRatingType.POLITENESS)) {
                politeness = userRatingProjection.getCount();
            } else if (userRatingProjection.getRatingType().equals(UserRatingType.LEARNING_ABILITY)) {
                learningAbility = userRatingProjection.getCount();
            } else if (userRatingProjection.getRatingType().equals(UserRatingType.RESPONSIBILITY)) {
                responsibility = userRatingProjection.getCount();
            } else if (userRatingProjection.getRatingType().equals(UserRatingType.CREATIVITY)) {
                creativity = userRatingProjection.getCount();
            }
        }

        return new UserRatingOut(
                politeness,
                learningAbility,
                responsibility,
                creativity

        );
    }

    public PageableOut<UserEvent> findAllUserEvents(long userId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1,
                limit,
                Sort.by("id").descending());
        Page<Event> events = eventRepository.findEventByUserId(userId,
                pageable);

        List<UserEvent> userEvents = new ArrayList<>();
        for (Event event : events.getContent()) {
            long membersCount = eventRepository.countMembersById(event.getId());
            userEvents.add(userMapper.toUserEvent(event, membersCount));
        }

        return new PageableOut<>(page,
                events.getSize(),
                events.getTotalPages(),
                events.getTotalElements(),
                userEvents);
    }

    public PageableOut<UserReviewOut> findAllUserReviews(long userId,
                                                         int page,
                                                         int limit) {
        Pageable pageable = PageRequest.of(page - 1,
                limit,
                Sort.by("createdAt").descending());

        Page<UserReview> reviews = userReviewRepository.findAllByRecipientId(
                userId,
                pageable
        );
        return new PageableOut<>(
                page,
                reviews.getSize(),
                reviews.getTotalPages(),
                reviews.getTotalElements(),
                userMapper.toUserReviewOut(reviews.getContent())
        );
    }

    public PageableOut<UserProject> findAllUserProjects(long userId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1,
                limit,
                Sort.by("createDate").descending());

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
                projects.getTotalElements(),
                userProject
        );
    }

    public void setRatingToUser(long userId, UserRatingIn userRatingIn, JwtAuthentication authentication) {
        final long userOwnerId = authentication.getUserId();
        final User recipient = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        final User sender = userRepository.findById(userOwnerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        // Если переменная isValid равна true, то пользователь может оценить другого пользователя
        boolean isValid = projectMemberRepository.existsByRecipientIdAndSenderId(userId, userOwnerId);
        if (!isValid) {
            throw new ConflictException("Вы не можете оценить этого пользователя");
        }

        if (!userRatingRepository.findBySenderIdAndRecipientId(userOwnerId, userId).isEmpty()) {
            throw new ConflictException("Вы уже оценили этого пользователя");
        }

        List<UserRating> userRatings = new ArrayList<>();
        for (int i = 0; i < userRatingIn.types().size(); i++) {
           userRatings.add(userMapper.toUserRating(sender, recipient, userRatingIn.types().get(i)));
        }
        userRatingRepository.saveAll(userRatings);
    }

    @Transactional
    public void updateUserMedia(UserMediaIn userMediaIn,
                                Authentication authentication
                                ) {
        long userIdOwner = ((JwtAuthentication) authentication).getUserId();
        User user = userRepository.findById(userIdOwner).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));

        user.getUserMedia().setPhone(userMediaIn.phone());
        user.getUserMedia().setVkUrl(userMediaIn.vkUrl());
        user.getUserMedia().setTgUrl(userMediaIn.tgUrl());
        if (!user.getEmail().equals(userMediaIn.email())) {
            if (userRepository.findByEmail(userMediaIn.email()).isPresent()) {
                throw new ConflictException("Такая почта уже занята");
            }
            if (userMediaIn.email() != null && !userMediaIn.email().isEmpty()) {
                user.setEmail(userMediaIn.email());
            }
        }

        userRepository.save(user);
    }

    public void updateUserAbout(UserAboutIn aboutIn,
                                Authentication authentication) {
        long userIdOwner = ((JwtAuthentication) authentication).getUserId();
        User user = userRepository.findById(userIdOwner).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));

        user.getUserMedia().setAbout(aboutIn.about());

        userRepository.save(user);
    }

    @Transactional
    public void addRoleToUser(long roleId,
                              JwtAuthentication authentication) {
        final User user = userRepository.findById(authentication.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        final RoleForProject roleForProject = roleForProjectRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Роль не найдена"));

        if (userRoleForProjectRepository.findByUserIdAndRoleId(user.getId(), roleId).isPresent()) {
            throw new ConflictException("Такая роль уже привязана");
        };

        UserRoleForProject userRoleForProject = new UserRoleForProject();
        userRoleForProject.setRole(roleForProject);
        userRoleForProject.setUser(user);

        user.getUserRolesForProject().add(userRoleForProject);
        userRoleForProjectRepository.save(userRoleForProject);
//        userRepository.save(user);
    }

    @Transactional
    public void removeRoleToUser(long roleId, JwtAuthentication authentication) {
        final User user = userRepository.findById(authentication.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        final RoleForProject roleForProject = roleForProjectRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Роль не найдена"));

        UserRoleForProject userRole = userRoleForProjectRepository.findByUserIdAndRoleId(user.getId(), roleForProject.getId())
                .orElseThrow(() -> new NotFoundException("Такая роль не привязана"));

        userRoleForProjectRepository.delete(userRole);
    }

    @Transactional
    public List<UserRoleForProjectOut> updateUserRoleForProjectById(long userId,
                                                                    List<UserRoleForProjectOut> roles,
                                                                    Authentication authentication) {
        long userIdOwner = ((JwtAuthentication) authentication).getUserId();
        if (userId != userIdOwner)
            throw new ForbiddenException("Вы не лидер проекта");

        final User user = userRepository.findById(userIdOwner)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        userRoleForProjectRepository.deleteByUserId(userIdOwner);

        for (UserRoleForProjectOut role : roles) {
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

        return findUserById(userId);
    }

    @Transactional
    public UserOut updateBackground(MultipartFile background, JwtAuthentication authentication) throws IOException {
        long userId = authentication.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        String filePath = userAvatarLoader.load(background);
        user.setBackground(filePath);
        userRepository.save(user);

        return findUserById(userId);
    }

    public List<UserOut> findAll(int page, int limit, String rolesSource){
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<User> users = null;

        if (rolesSource.equals("all")){
            users = userRepository.findAll(pageable);
        }
        else{
            List<Integer> rolesList = Arrays.asList(rolesSource.split(";"))
                    .stream().map(item -> Integer.parseInt(item)).toList();

            users = userRepository.findAllByRoleForProject(pageable, rolesList);
        }

        List<UserOut> usersOut = new ArrayList<>();
        for (User user : users){
            usersOut.add(userMapper.toUserOut(user, null, null));
        }

        return usersOut;
    }
}
