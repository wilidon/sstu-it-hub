package ru.sstu.studentprofile.data.repository.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sstu.studentprofile.data.models.event.Event;
import ru.sstu.studentprofile.data.models.event.EventStatus;
import ru.sstu.studentprofile.data.repository.event.projection.EventMembers;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByStatusOrderByStartDateDesc(EventStatus eventStatus,
                                                    Pageable pageable);

    @Query("""
            select u.id as id, u.login as name, u.email as avatar
            from Event e
                join Project p on e.id = p.event.id
                join ProjectMember pm on p.id = pm.project.id
                join User u on pm.user.id = u.id
            where e.id = :eventId 
""")
    List<EventMembers> findMembersById(long eventId, Pageable pageable);

}
