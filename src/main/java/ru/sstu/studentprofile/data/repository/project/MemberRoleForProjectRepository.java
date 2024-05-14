package ru.sstu.studentprofile.data.repository.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sstu.studentprofile.data.models.project.MemberRoleForProject;
import ru.sstu.studentprofile.data.repository.project.proection.Role;

@Repository
public interface MemberRoleForProjectRepository extends JpaRepository<MemberRoleForProject, Long> {
    @Query("SELECT m.role.name as name, COUNT(m.role.name) as count FROM MemberRoleForProject m GROUP BY m.role.name ORDER BY COUNT(m.role.name) DESC LIMIT 1")
    Role getTopRole();

    @Query("SELECT m.role.name as name, COUNT(m.role.name) as count  FROM MemberRoleForProject m GROUP BY m.role.name ORDER BY COUNT(m.role.name) ASC LIMIT 1")
    Role getRareRole();
}
