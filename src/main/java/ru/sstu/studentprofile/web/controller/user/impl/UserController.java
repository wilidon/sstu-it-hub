package ru.sstu.studentprofile.web.controller.user.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.user.UserService;
import ru.sstu.studentprofile.domain.service.user.dto.UserOut;
import ru.sstu.studentprofile.domain.service.user.dto.UserRoleForProjectOut;
import ru.sstu.studentprofile.web.controller.user.UserApi;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<UserOut> getUserById(final long userId) {
        return ResponseEntity.ok(userService.findUserById(userId));
    }

    @Override
    public ResponseEntity<UserOut> me(final JwtAuthentication authentication) {
        return ResponseEntity.ok(userService.findMe(authentication));
    }

    @Override
    public ResponseEntity<?> userEvents(long userId,
                                        int size,
                                        int limit) {
        return ResponseEntity.ok(userService.findAllUserEvents(userId, size, limit));
    }

    @Override
    public ResponseEntity<?> userProjects(long userId, int size, int limit) {
        return ResponseEntity.ok(userService.findAllUserProjects(userId, size, limit));
    }

    @Override
    public ResponseEntity<List<UserRoleForProjectOut>> updateUserRoleForProjectById(long userId, List<UserRoleForProjectOut> roles, Authentication authentication) {
        return ResponseEntity.ok(userService.updateUserRoleForProjectById(userId, roles, authentication));
    }
}
