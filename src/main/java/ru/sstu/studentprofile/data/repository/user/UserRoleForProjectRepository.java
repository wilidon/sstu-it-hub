package ru.sstu.studentprofile.data.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sstu.studentprofile.data.models.project.RoleForProject;
import ru.sstu.studentprofile.data.models.user.UserRoleForProject;

@Repository
public interface UserRoleForProjectRepository extends JpaRepository<UserRoleForProject, Long> {
    @Modifying
    @Query("DELETE FROM UserRoleForProject r WHERE r.user.id = :userId")
    public void deleteByUserId(long userId);
}
