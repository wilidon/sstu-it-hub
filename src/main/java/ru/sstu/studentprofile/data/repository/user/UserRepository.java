package ru.sstu.studentprofile.data.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sstu.studentprofile.data.models.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
