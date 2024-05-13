package ru.sstu.studentprofile.data.repository.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sstu.studentprofile.data.models.event.Event;
import ru.sstu.studentprofile.data.models.project.Project;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAllByOrderByCreateDateDesc(Pageable pageable);

    @Query("SELECT COUNT(p) FROM Project p")
    long getCountAllProject();

    @Query("SELECT COUNT(p) FROM Project p WHERE DATE(p.createDate) = :localDate")
    long getCountAllProjectToday(LocalDate localDate);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = 'PLANNED'")
    long getCountAllProjectPlanned();

    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = 'OPEN'")
    long getCountAllProjectOpen();

    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = 'COMPLETED'")
    long getCountAllProjectCompleted();

    @Query("""
    select p from Project p 
    join ProjectMember pm on p.id = pm.project.id
    where pm.user.id = :userId
""")
    Page<Project> findAllProjectsByUserId(@Param("userId") long userId, Pageable pageable);

    @Query("""
        select p from Project p
        where p.event.id = :eventId
""")
    Page<Project> findAllProjectsByEventId(long eventId, Pageable pageable);


}
