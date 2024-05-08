package ru.sstu.studentprofile.domain.service.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.data.repository.user.UserRepository;
import ru.sstu.studentprofile.domain.exception.NotFoundException;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.security.UserDetailsImpl;
import ru.sstu.studentprofile.domain.service.user.dto.UserOut;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserOut findUserById(final long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

       return userMapper.toUserOut(user);
    }


    public UserOut findMe(final @NotNull JwtAuthentication authentication) {
        final User user = ((UserDetailsImpl) authentication.getPrincipal()).user();

        return findUserById(user.getId());
    }

}
