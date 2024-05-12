package ru.sstu.studentprofile.data.repository.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sstu.studentprofile.data.models.project.MemberRoleForProject;

@Repository
public interface MemberRoleForProjectRepository extends JpaRepository<MemberRoleForProject, Long> {
    @Query("SELECT m.role.name FROM MemberRoleForProject m GROUP BY m.role.name ORDER BY m.role.name DESC LIMIT 1")
    String getTopRole();

    @Query("SELECT m.role.name FROM MemberRoleForProject m GROUP BY m.role.name ORDER BY m.role.name ASC LIMIT 1")
    String getRareRole();
}
