package ru.sstu.studentprofile.web.controller.user.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.user.UserService;
import ru.sstu.studentprofile.domain.service.user.dto.UserAboutIn;
import ru.sstu.studentprofile.domain.service.user.dto.UserMediaIn;
import ru.sstu.studentprofile.domain.service.user.dto.UserOut;
import ru.sstu.studentprofile.domain.service.user.dto.UserRoleForProjectOut;
import ru.sstu.studentprofile.domain.service.user.dto.rating.UserRatingIn;
import ru.sstu.studentprofile.web.controller.user.UserApi;

import java.io.IOException;
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
                                        int page,
                                        int limit) {
        return ResponseEntity.ok(userService.findAllUserEvents(userId, page, limit));
    }

    @Override
    public ResponseEntity<?> userProjects(long userId, int page, int limit) {
        return ResponseEntity.ok(userService.findAllUserProjects(userId, page, limit));
    }

    @Override
    public ResponseEntity<?> addRoleForProjects(long roleId, JwtAuthentication authentication) {
        userService.addRoleToUser(roleId, authentication);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<?> deleteRoleForProjects(long roleId, JwtAuthentication authentication) {
        userService.removeRoleToUser(roleId, authentication);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<?> userReviews(long userId, int page, int limit) {
        return ResponseEntity.ok(userService.findAllUserReviews(userId, page, limit));
    }

    @Override
    public ResponseEntity<List<UserRoleForProjectOut>> updateUserRoleForProjectById(long userId, List<UserRoleForProjectOut> roles, Authentication authentication) {
        return ResponseEntity.ok(userService.updateUserRoleForProjectById(userId, roles, authentication));
    }

    @Override
    public ResponseEntity<?> updateUserMedia(UserMediaIn aboutIn,
                                             JwtAuthentication authentication) {
        userService.updateUserMedia(aboutIn, authentication);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<?> updateUserAbout(UserAboutIn aboutIn, JwtAuthentication authentication) {
        userService.updateUserAbout(aboutIn, authentication);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<?> addRating(long userId, UserRatingIn userRatingIn, JwtAuthentication authentication) {
        userService.setRatingToUser(userId, userRatingIn, authentication);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<UserOut> updateAvatar(MultipartFile avatar, JwtAuthentication authentication) throws IOException {
        return ResponseEntity.ok(userService.updateAvatar(avatar, authentication));
    }

    @Override
    public ResponseEntity<UserOut> updateBackground(MultipartFile background, JwtAuthentication authentication) throws IOException {
        return ResponseEntity.ok(userService.updateBackground(background, authentication));
    }

    @Override
    public ResponseEntity<?> all(int page, int limit, String roles, String search) {
        return ResponseEntity.ok(userService.findAll(page, limit, roles, search));
    }
}
