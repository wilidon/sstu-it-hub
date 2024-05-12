package ru.sstu.studentprofile.web.controller.request.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sstu.studentprofile.domain.service.request.RequestService;
import ru.sstu.studentprofile.domain.service.request.dto.RequestIn;
import ru.sstu.studentprofile.domain.service.request.dto.RequestOut;
import ru.sstu.studentprofile.domain.service.request.dto.RequestResultIn;
import ru.sstu.studentprofile.domain.service.roleForProject.dto.RoleForProjectOut;
import ru.sstu.studentprofile.web.controller.request.RequestApi;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/request")
@CrossOrigin("*")
public class RequestController implements RequestApi {
    private final RequestService requestService;

    @Override
    public ResponseEntity<RequestOut> create(RequestIn requestIn, Authentication authentication) {
        return ResponseEntity.ok(requestService.create(requestIn, authentication));
    }

    @Override
    public ResponseEntity<RequestOut> patchRequest(long requestId, RequestResultIn requestResultIn, Authentication authentication) {
        return ResponseEntity.ok(requestService.patchRequest(requestId, requestResultIn, authentication));
    }

    @Override
    public ResponseEntity<List<RequestOut>> all(Authentication authentication) {
        return ResponseEntity.ok(requestService.all(authentication));
    }
}
