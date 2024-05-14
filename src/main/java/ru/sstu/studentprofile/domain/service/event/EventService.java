package ru.sstu.studentprofile.domain.service.event;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sstu.studentprofile.data.models.event.Event;
import ru.sstu.studentprofile.data.models.event.EventStatus;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.data.repository.event.EventRepository;
import ru.sstu.studentprofile.data.repository.event.projection.EventMembers;
import ru.sstu.studentprofile.data.repository.project.ProjectRepository;
import ru.sstu.studentprofile.data.repository.user.UserRepository;
import ru.sstu.studentprofile.domain.exception.ForbiddenException;
import ru.sstu.studentprofile.domain.exception.NotFoundException;
import ru.sstu.studentprofile.domain.exception.UnprocessableEntityException;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;
import ru.sstu.studentprofile.domain.service.event.dto.EventIn;
import ru.sstu.studentprofile.domain.service.event.dto.EventOut;
import ru.sstu.studentprofile.domain.service.event.dto.EventStatusIn;
import ru.sstu.studentprofile.domain.service.event.dto.FilterStatusIn;
import ru.sstu.studentprofile.domain.service.event.dto.ShortEventOut;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectOut;
import ru.sstu.studentprofile.domain.service.project.mappers.ProjectMapper;
import ru.sstu.studentprofile.domain.service.storage.FileLoader;
import ru.sstu.studentprofile.domain.service.util.PageableOut;

import java.io.IOException;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final ProjectRepository projectRepository;
    private final EventMapper mapper;
    private final ProjectMapper projectMapper;
    private final FileLoader fileLoader;
    private final UserRepository userRepository;

    @Autowired
    public EventService(EventRepository eventRepository, ProjectRepository projectRepository,
                        EventMapper mapper, ProjectMapper projectMapper,
                        @Qualifier("eventAvatarLoader") FileLoader fileLoader, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.projectRepository = projectRepository;
        this.mapper = mapper;
        this.projectMapper = projectMapper;
        this.fileLoader = fileLoader;
        this.userRepository = userRepository;
    }

    public PageableOut<ShortEventOut> all(String query, boolean needActualRoles, int page, int limit, FilterStatusIn eventStatusIn) {
        query = query.toLowerCase();
        Pageable pageable = PageRequest.of(page - 1,
                limit,
                Sort.by("endDate").ascending());
        Page<Event> events;

        // TODO Переписать потом на CriteriaApi
        if (eventStatusIn == FilterStatusIn.ALL && query.isEmpty()) {
            // Фильтры всё, в query - пусто
            events = eventRepository.findAll(pageable);
            return new PageableOut<>(
                    page,
                    events.getSize(),
                    events.getTotalPages(),
                    events.getTotalElements(),
                    mapper.toShortEventOut(events.getContent())
            );
        }

        else if (eventStatusIn == FilterStatusIn.ALL && !query.isBlank()) {
            // Фильтры == ALL, а query не пустой
            events = eventRepository.findAllByQuery(query, pageable);
            return new PageableOut<>(
                    page,
                    events.getSize(),
                    events.getTotalPages(),
                    events.getTotalElements(),
                    mapper.toShortEventOut(events.getContent())
            );
        }
        else if (eventStatusIn != FilterStatusIn.ALL && query.isEmpty()) {
            // Фильтры != ALL, query - пусто
            EventStatus eventStatus = EventStatus.fromString(eventStatusIn.name());
            events = eventRepository.findAllByStatusOrderByStartDateDesc(eventStatus, pageable);
        }
        else {
            // Фильтры != ALL, query - есть
            EventStatus eventStatus = EventStatus.fromString(eventStatusIn.name());
            events = eventRepository.findAllByStatusAndQuery(query, eventStatus, pageable);
        }
        return new PageableOut<>(
                page,
                events.getSize(),
                events.getTotalPages(),
                events.getTotalElements(),
                mapper.toShortEventOut(events.getContent())
        );
    }

    public EventOut findById(long id) {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Мероприятие с id=%d не найдено".formatted(id))
        );

        List<EventMembers> eventMembers = eventRepository.findMembersById(id,
                PageRequest.of(0, 6));
        long membersCount = eventRepository.countMembersById(id);

        Page<Project> projects =
                projectRepository.findAllProjectsByEventId(id, PageRequest.of(0, 6));
        return mapper.toEventOut(event, membersCount, eventMembers, projects.getContent());
    }

    public PageableOut<ProjectOut> getProjects(long eventId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Project> projects = projectRepository.findAll(pageable);

        return new PageableOut<>(
                page,
                projects.getSize(),
                projects.getTotalPages(),
                projects.getTotalElements(),
                projectMapper.toProjectOut(projects.getContent())
        );
    }

    @Transactional
    public EventOut create(EventIn eventIn,
                           Authentication authentication) {
        long userId = ((JwtAuthentication) authentication).getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(userId)));

        Event event = mapper.toEvent(eventIn, user);

        Event savedEvent = eventRepository.save(event);
        return findById(savedEvent.getId());
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
