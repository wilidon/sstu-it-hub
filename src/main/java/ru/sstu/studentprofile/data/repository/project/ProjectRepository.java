package ru.sstu.studentprofile.data.repository.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sstu.studentprofile.data.models.event.Event;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.project.ProjectStatus;
import ru.sstu.studentprofile.data.models.project.RoleForProject;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectStatusSearchIn;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    @Query("SELECT p FROM Project p WHERE (:status is NULL OR p.status = :status) ORDER BY p.createDate DESC")
    Page<Project> findAllByOrderByCreateDateDesc(Pageable pageable, ProjectStatus status);

    @Query("""
        select ac.project from ActualRoleForProject ac WHERE ac.role.name = :nameRole GROUP BY ac.project
""")
    Page<Project> findAllByActualRoleMemberProject(Pageable pageable, String nameRole);

    @Query("""
        select p from Project p WHERE p.status = :status
""")
    Page<Project> findAllByStatusProject(Pageable pageable, String status);

    @Query("select ac.project from ActualRoleForProject ac WHERE (:status is NULL OR ac.project.status = :status) GROUP BY ac.project")
    Page<Project> findAllByActualRoleProject(Pageable pageable, ProjectStatus status);

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
                    order by p.createDate desc
            """)
    Page<Project> findAllProjectsByEventId(long eventId, Pageable pageable);

    @Query("SELECT p FROM Project p WHERE EXISTS (SELECT 1 FROM UserRoleForProject u WHERE u.user.id = :userId AND u.role IN (SELECT ac.role FROM ActualRoleForProject ac WHERE ac.project.id = p.id))")
    Page<Project> findAllByRoleForProject(Pageable pageable, long userId);
}
