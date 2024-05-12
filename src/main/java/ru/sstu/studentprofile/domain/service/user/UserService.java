package ru.sstu.studentprofile.domain.service.user;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.sstu.studentprofile.data.models.project.RoleForProject;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.data.models.user.UserRoleForProject;
import ru.sstu.studentprofile.data.repository.project.RoleForProjectRepository;
import ru.sstu.studentprofile.data.repository.user.UserRepository;
import ru.sstu.studentprofile.data.repository.user.UserRoleForProjectRepository;
import ru.sstu.studentprofile.domain.exception.ForbiddenException;
import ru.sstu.studentprofile.domain.exception.NotFoundException;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.security.UserDetailsImpl;
import ru.sstu.studentprofile.domain.service.user.dto.UserOut;
import ru.sstu.studentprofile.domain.service.user.dto.UserRoleForProjectOut;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserRoleForProjectRepository userRoleForProjectRepository;
    private final RoleForProjectRepository roleForProjectRepository;

    public UserOut findUserById(final long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

       return userMapper.toUserOut(user);
    }


    public UserOut findMe(final @NotNull JwtAuthentication authentication) {
        final User user = ((UserDetailsImpl) authentication.getPrincipal()).user();

        return findUserById(user.getId());
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

}
