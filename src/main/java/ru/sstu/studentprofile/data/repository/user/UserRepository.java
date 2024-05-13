package ru.sstu.studentprofile.data.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sstu.studentprofile.data.models.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT COUNT(u) FROM User u")
    long getCountAllUsers();

    Optional<User> findByEmail(String email);
}
