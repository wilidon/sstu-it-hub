package ru.sstu.studentprofile.data.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sstu.studentprofile.data.models.user.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
