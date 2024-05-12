package ru.sstu.studentprofile.domain.service.request;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.project.ProjectMember;
import ru.sstu.studentprofile.data.models.request.Request;
import ru.sstu.studentprofile.data.models.request.RequestResult;
import ru.sstu.studentprofile.data.models.request.RequestType;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.data.repository.project.ProjectMemberRepository;
import ru.sstu.studentprofile.data.repository.project.ProjectRepository;
import ru.sstu.studentprofile.data.repository.request.RequestRepository;
import ru.sstu.studentprofile.data.repository.user.UserRepository;
import ru.sstu.studentprofile.domain.exception.ForbiddenException;
import ru.sstu.studentprofile.domain.exception.NotFoundException;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.request.dto.RequestIn;
import ru.sstu.studentprofile.domain.service.request.dto.RequestOut;
import ru.sstu.studentprofile.domain.service.request.dto.RequestResultIn;
import ru.sstu.studentprofile.domain.service.request.dto.RequestTypeIn;
import ru.sstu.studentprofile.domain.service.request.mappers.RequestMapper;
import ru.sstu.studentprofile.domain.service.request.mappers.SenderMapper;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper mapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final SenderMapper mapperSender;

    @Transactional
    public RequestOut create(RequestIn requestIn, Authentication authentication){
        Project project = projectRepository.findById(requestIn.projectId())
                .orElseThrow(() -> new NotFoundException("Проект с id=%d не найдено".formatted(requestIn.projectId())));

        long userId = ((JwtAuthentication) authentication).getUserId();

        if (requestIn.type() == RequestTypeIn.INVITE && userId != project.getLeader().getId())
            throw new ForbiddenException("Вы не лидер проекта");

        if (requestIn.type() == RequestTypeIn.REQUEST && project.getLeader().getId() != requestIn.recipientId())
            throw new ForbiddenException("Нет права на это");

        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найдено".formatted(userId)));
        User recipient = userRepository.findById(requestIn.recipientId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найдено".formatted(requestIn.recipientId())));


        Request request = mapper.toRequest(requestIn, sender, recipient, project);
        request.setResult(RequestResult.WAIT);

        requestRepository.save(request);

        return mapper.toRequestOut(request, mapperSender);
    }

    @Transactional
    public RequestOut patchRequest(long requestId, RequestResultIn requestResultIn, Authentication authentication){
        long userId = ((JwtAuthentication) authentication).getUserId();

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Заявка с id=%d не найдена".formatted(requestId)));
        if (request.getRecipient().getId() != userId)
            throw new ForbiddenException("Нет права на это");

        request.setResult(RequestResult.fromRequestResultIn(requestResultIn));
        requestRepository.save(request);

        if (requestResultIn == RequestResultIn.ACEEPTED){
            Project project = projectRepository.findById(request.getProject().getId())
                    .orElseThrow(() -> new NotFoundException("Проект с id=%d не найден".formatted(request.getProject().getId())));

            User user = null;

            if (request.getType() == RequestType.INVITE){
                user = userRepository.findById(userId)
                        .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(userId)));
            }
            else if (request.getType() == RequestType.REQUEST){
                user = userRepository.findById(request.getSender().getId())
                        .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(userId)));
            }

            ProjectMember member = new ProjectMember();
            member.setProject(project);
            member.setUser(user);

            projectMemberRepository.save(member);
        }

        return mapper.toRequestOut(request, mapperSender);
    }

    public List<RequestOut> all(Authentication authentication){
        long userId = ((JwtAuthentication) authentication).getUserId();

        List<Request> requests = requestRepository.getAllByIdUser(userId);

        List<RequestOut> requestOutList = new ArrayList<>();
        for (Request request : requests) {
            requestOutList.add(mapper.toRequestOut(request, mapperSender));
        }

        return requestOutList;
    }
}

//TODO ДОБАВИТЬ ПРОЕКТ В СУЩНОСТЬ