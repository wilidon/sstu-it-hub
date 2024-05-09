package ru.sstu.studentprofile.domain.service.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.event.Event;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.domain.service.event.dto.EventIn;
import ru.sstu.studentprofile.domain.service.event.dto.EventOut;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {

    @Mapping(target = "membersCount", source = "membersCount")
    EventOut toEventOut(Event event, Long membersCount);

    List<EventOut> toEventOut(List<Event> events);

    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", source = "user")
    Event toEvent(EventIn eventIn, User user);
}
