package ru.sstu.studentprofile.data.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sstu.studentprofile.data.models.project.RoleForProject;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.domain.service.util.PageableOut;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT COUNT(u) FROM User u")
    long getCountAllUsers();

    Optional<User> findByEmail(String email);

    @Query("""
        SELECT DISTINCT u FROM User u 
            INNER JOIN UserRoleForProject ur ON u.id = ur.user.id
        WHERE ur.role.id IN :roles
""")
    Page<User> findAllByRoleForProject(Pageable pageable, List<Integer> roles);
}
