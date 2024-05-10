package ru.sstu.studentprofile.web.controller.user.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.user.UserService;
import ru.sstu.studentprofile.domain.service.user.dto.UserOut;
import ru.sstu.studentprofile.web.controller.user.UserApi;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
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

}
