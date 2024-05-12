package ru.sstu.studentprofile.data.repository.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sstu.studentprofile.data.models.project.ActualRoleForProject;

@Repository
public interface ActualRoleForProjectRepository extends JpaRepository<ActualRoleForProject, Long> {
    @Modifying
    @Query("DELETE FROM ActualRoleForProject ar WHERE ar.project.id = :projectId")
    public void deleteByIdProject(long projectId);

    @Query("SELECT ar.role.id FROM ActualRoleForProject ar GROUP BY ar.role.id ORDER BY COUNT(ar.role.id) DESC LIMIT 1")
    Long getFindestRole();
}
