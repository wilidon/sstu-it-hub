package ru.sstu.studentprofile.data.repository.user;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.sstu.studentprofile.data.models.user.User;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {

    boolean existsByLogin(String login);
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    Optional<User> findByLogin(String login);
}
