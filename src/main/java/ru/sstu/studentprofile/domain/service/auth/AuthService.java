package ru.sstu.studentprofile.domain.service.auth;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.data.repository.user.AuthRepository;
import ru.sstu.studentprofile.data.repository.user.UserRepository;
import ru.sstu.studentprofile.domain.exception.ConflictException;
import ru.sstu.studentprofile.domain.exception.ForbiddenException;
import ru.sstu.studentprofile.domain.security.JwtProvider;
import ru.sstu.studentprofile.domain.security.UserDetailsImpl;
import ru.sstu.studentprofile.domain.security.dto.TokenOut;
import ru.sstu.studentprofile.domain.service.auth.dto.BearerLoginOut;
import ru.sstu.studentprofile.domain.service.auth.dto.LoginIn;
import ru.sstu.studentprofile.domain.service.auth.dto.RefreshAccessTokenIn;
import ru.sstu.studentprofile.domain.service.auth.dto.RegisterIn;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    private final UserDetailsService userDetailsService;

    @Transactional
    public BearerLoginOut login(final LoginIn loginIn) throws AuthenticationException {
        final Authentication authentication = authManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginIn.login(),
                                loginIn.password()));
        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        final TokenOut refreshToken = jwtProvider.generateRefreshToken(userDetails);
        final TokenOut accessToken = jwtProvider.generateAccessToken(userDetails);
        return new BearerLoginOut(
                accessToken.token(),
                accessToken.expires(),
                refreshToken.token(),
                refreshToken.expires(),
                userDetails.user().getUserRoles()
                        .stream()
                        .map(it -> it.getRole().getName())
                        .collect(Collectors.toSet())
        );
    }

    @Transactional
    public BearerLoginOut register(final RegisterIn registerIn) {
        final User user = new User();
        if (authRepository.existsByEmail(registerIn.email())) {
            throw new ConflictException("Такая почта уже зарегистрирована.");
        }

        user.setEmail(registerIn.email());

        if (registerIn.login() != null) {
            if (authRepository.existsByLogin(registerIn.login())) {
                throw new ConflictException("Такое имя пользователя уже зарегистрировано.");
            }
            user.setLogin(registerIn.login());
        }
        user.setPassword(passwordEncoder.encode(registerIn.password()));

        userRepository.save(user);
        return login(new LoginIn(user.getLogin(), registerIn.password()));
    }

    @Transactional
    public BearerLoginOut refresh(final RefreshAccessTokenIn refreshAccessTokenIn) {
        if (!jwtProvider.validateRefreshToken(refreshAccessTokenIn.refreshToken()) ||
                !jwtProvider.validateExpiredAccessToken(refreshAccessTokenIn.accessToken())) {
            throw new ForbiddenException("Токен доступа или токен обновления недействителен.");
        }

        final UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(
                jwtProvider.extractUsernameRefreshClaim(refreshAccessTokenIn.refreshToken())
        );
        final TokenOut accessToken = jwtProvider.generateAccessToken(userDetails);
        return new BearerLoginOut(
                accessToken.token(),
                accessToken.expires(),
                refreshAccessTokenIn.refreshToken(),
                0L,
                userDetails.user().getUserRoles()
                        .stream()
                        .map(it -> it.getRole().getName())
                        .collect(Collectors.toSet())
        );
    }
}
