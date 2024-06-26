package ru.sstu.studentprofile.web.controller.event.impl;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.sstu.studentprofile.domain.service.event.EventService;
import ru.sstu.studentprofile.domain.service.event.dto.EventIn;
import ru.sstu.studentprofile.domain.service.event.dto.EventOut;
import ru.sstu.studentprofile.domain.service.event.dto.EventStatusIn;
import ru.sstu.studentprofile.domain.service.event.dto.FilterStatusIn;
import ru.sstu.studentprofile.domain.service.event.dto.ShortEventOut;
import ru.sstu.studentprofile.domain.service.util.PageableOut;
import ru.sstu.studentprofile.web.controller.event.EventApi;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class EventController implements EventApi {

    private final EventService eventService;

    @Override
    public ResponseEntity<PageableOut<ShortEventOut>> getAllEvents(String query,
                                                                   final int page,
                                                                   final int limit,
                                                                   FilterStatusIn eventStatusIn) {
        return ResponseEntity.ok(eventService.all(query, page, limit, eventStatusIn));
    }

    @Override
    public ResponseEntity<?> getEventById(final long id) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    @Override
    public ResponseEntity<?> getEventProjects(long id, int page, int limit) {
        return ResponseEntity.ok(eventService.getProjects(id, page, limit));
    }

    @Override
    public ResponseEntity<?> create(final EventIn eventIn,
                                    Authentication authentication) {
        return ResponseEntity.ok(eventService.create(eventIn, authentication));
    }

    @Override
    public ResponseEntity<?> updateEvent(long eventId, EventIn eventIn, Authentication authentication) {
        return ResponseEntity.ok(eventService.update(eventId, eventIn, authentication));
    }

    @Override
    public ResponseEntity<?> updateEventStatus(long eventId, EventStatusIn eventStatusIn, Authentication authentication) {
        return ResponseEntity.ok(eventService.updateStatus(eventId, eventStatusIn, authentication));
    }


    @Override
    public ResponseEntity<EventOut> uploadFile(
            final long eventId,
            Authentication authentication,
            final MultipartFile avatar) throws IOException {
        return ResponseEntity.ok(
                eventService.uploadAvatar(eventId,
                        authentication,
                        avatar)
        );
    }

    @Override
    public ResponseEntity<EventOut> deleteAvatar(
            final long eventId,
            Authentication authentication) {
        return ResponseEntity.ok(
                eventService.deleteAvatar(eventId,
                        authentication)
        );
    }
}
