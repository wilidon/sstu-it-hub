package ru.sstu.studentprofile.domain.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.data.repository.user.AuthRepository;

@Component
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
        final User user = authRepository
                .findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        return new UserDetailsImpl(user);
    }
}
