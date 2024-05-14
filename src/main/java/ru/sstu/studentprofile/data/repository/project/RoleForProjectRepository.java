package ru.sstu.studentprofile.data.repository.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sstu.studentprofile.data.models.project.RoleForProject;

import java.util.List;

@Repository
public interface RoleForProjectRepository extends JpaRepository<RoleForProject, Long> {
    @Query("SELECT r FROM UserRoleForProject r WHERE r.user.id = :userId")
    List<RoleForProject> getRoleForProjectByUserId(long userId);
}
