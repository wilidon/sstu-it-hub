package ru.sstu.studentprofile.data.repository.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sstu.studentprofile.data.models.event.Event;
import ru.sstu.studentprofile.data.models.event.EventStatus;
import ru.sstu.studentprofile.data.repository.event.projection.EventMembers;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("""
            select e from Event e
            where lower(e.name) like %:query%
            """)
    Page<Event> findAllByQuery(@Param("query") String query, Pageable pageable);

    Page<Event> findAllByStatusOrderByStartDateDesc(EventStatus eventStatus,
                                                    Pageable pageable);



    @Query("""
            select e from Event e
            where lower(e.name) like %:query%
            and e.status = :eventStatus
            """)
    Page<Event> findAllByStatusAndQuery(
            String query,
            EventStatus eventStatus,
            Pageable pageable);

    @Query("""
                        select u.id as id, u.login as name, u.avatar as avatar
                        from Event e
                            join Project p on e.id = p.event.id
                            join ProjectMember pm on p.id = pm.project.id
                            join User u on pm.user.id = u.id
                        where e.id = :eventId 
            """)
    List<EventMembers> findMembersById(long eventId, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.id = (SELECT p.event.id FROM Project p GROUP BY p.event.id ORDER BY COUNT(p.event.id) DESC LIMIT 1)")
    Event findTopEventByMembers();

    @Query("""
            select e from Event e
                join Project p on p.event.id = e.id
            join ProjectMember pm on p.id = pm.project.id
            join User u on pm.user.id = u.id
            where u.id = :userId
            """)
    Page<Event> findEventByUserId(@Param("userId") long userId, Pageable pageable);

    @Query("""
                        select count(u.id) from Event e
                            join Project p on e.id = p.event.id
                            join ProjectMember pm on p.id = pm.project.id
                            join User u on pm.user.id = u.id
                        where e.id = :eventId
            """)
    long countMembersById(long eventId);


}
