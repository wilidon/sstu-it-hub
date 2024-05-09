package ru.sstu.studentprofile.domain.service.event;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sstu.studentprofile.data.models.event.Event;
import ru.sstu.studentprofile.data.models.event.EventStatus;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.data.models.user.UserRole;
import ru.sstu.studentprofile.data.repository.event.EventRepository;
import ru.sstu.studentprofile.data.repository.user.UserRepository;
import ru.sstu.studentprofile.domain.exception.ForbiddenException;
import ru.sstu.studentprofile.domain.exception.NotFoundException;
import ru.sstu.studentprofile.domain.exception.UnprocessableEntityException;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.event.dto.EventIn;
import ru.sstu.studentprofile.domain.service.event.dto.EventOut;
import ru.sstu.studentprofile.domain.service.event.dto.EventStatusIn;
import ru.sstu.studentprofile.domain.service.storage.FileLoader;
import ru.sstu.studentprofile.domain.service.user.UserService;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper mapper;
    private final FileLoader fileLoader;
    private final UserRepository userRepository;

    @Autowired
    public EventService(EventRepository eventRepository,
                        EventMapper mapper,
                        @Qualifier("eventAvatarLoader") FileLoader fileLoader, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.mapper = mapper;
        this.fileLoader = fileLoader;
        this.userRepository = userRepository;
    }

    public List<EventOut> all(int page) {
        short PAGE_SIZE = 25;
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE, Sort.by("startDate").descending());
        List<Event> events = eventRepository.findAllByOrderByStartDateDesc(pageable);
        return mapper.toEventOut(events);
    }

    public EventOut findById(long id) {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Мероприятие с id=%d не найдено".formatted(id))
        );

        return mapper.toEventOut(event, 100L);
    }

    @Transactional
    public EventOut create(EventIn eventIn,
                           Authentication authentication) {
        long userId = ((JwtAuthentication) authentication).getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(userId)));

        Event event = mapper.toEvent(eventIn, user);

        eventRepository.save(event);
        return mapper.toEventOut(event, 100L);
    }

    @Transactional
    public EventOut update(long eventId,
                           EventIn eventIn,
                           Authentication authentication) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Мероприятие с id=%d не найдено".formatted(eventId)));
        long userId = ((JwtAuthentication) authentication).getUserId();

        if (event.getAuthor().getId() != userId) {
            throw new ForbiddenException("Вы не являетесь автором мероприятия");
        }

        event.setName(eventIn.name());
        event.setDescription(eventIn.description());
        event.setStartDate(eventIn.startDate());
        event.setEndDate(eventIn.endDate());
        event.setStatus(EventStatus.fromEventStatusIn(eventIn.status()));

        eventRepository.save(event);
        return this.findById(eventId);
    }
    @Transactional
    public EventOut updateStatus(long eventId,
                                 EventStatusIn eventStatusIn,
                                 Authentication authentication) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Мероприятие с id=%d не найдено".formatted(eventId)));
        long userId = ((JwtAuthentication) authentication).getUserId();

        if (event.getAuthor().getId() != userId) {
            throw new ForbiddenException("Вы не являетесь автором мероприятия");
        }

        event.setStatus(EventStatus.fromEventStatusIn(eventStatusIn));
        eventRepository.save(event);
        return this.findById(eventId);
    }

    public EventOut uploadAvatar(final long eventId,
                                 final Authentication authentication,
                                 final MultipartFile file) throws IOException {
        final Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Мероприятие с id=%d не найдено".formatted(eventId)));
        long userId = ((JwtAuthentication) authentication).getUserId();

        if (event.getAuthor().getId() != userId) {
            throw new ForbiddenException("Вы не являетесь автором мероприятия");
        }

        if (file.isEmpty()) {
            throw new UnprocessableEntityException("Файл не загружен");
        }

        String filePath = fileLoader.load(file);
        event.setAvatar(filePath);
        eventRepository.save(event);
        return this.findById(eventId);
    }

    public EventOut deleteAvatar(long eventId,
                                 final Authentication authentication) {
        final Event event = eventRepository.findById(((JwtAuthentication) authentication).getUserId())
                .orElseThrow(() -> new NotFoundException("Мероприятие с id=%d не найдено".formatted(eventId)));
        event.setAvatar(null);
        eventRepository.save(event);
        return this.findById(eventId);
    }
}
