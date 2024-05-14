package ru.sstu.studentprofile.data.repository.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sstu.studentprofile.data.models.project.ActualRoleForProject;
import ru.sstu.studentprofile.data.repository.project.proection.Role;

@Repository
public interface ActualRoleForProjectRepository extends JpaRepository<ActualRoleForProject, Long> {
    @Modifying
    @Query("DELETE FROM ActualRoleForProject ar WHERE ar.project.id = :projectId")
    public void deleteByIdProject(long projectId);

    @Query("SELECT ar.role.name as name, COUNT(ar.role.id) as count FROM ActualRoleForProject ar GROUP BY ar.role.name ORDER BY COUNT(ar.role.name) DESC LIMIT 1")
    Role getFindestRole();
}
