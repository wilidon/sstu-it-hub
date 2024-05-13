package ru.sstu.studentprofile.data.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sstu.studentprofile.data.models.user.UserRating;

import java.util.List;
import java.util.Optional;

public interface UserRatingRepository extends JpaRepository<UserRating, Long> {
    List<UserRating> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
}
