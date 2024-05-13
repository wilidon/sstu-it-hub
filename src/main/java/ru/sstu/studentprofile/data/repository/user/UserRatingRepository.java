package ru.sstu.studentprofile.data.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sstu.studentprofile.data.models.user.UserRating;
import ru.sstu.studentprofile.data.repository.user.projection.UserRatingProjection;

import java.util.List;
import java.util.Optional;

public interface UserRatingRepository extends JpaRepository<UserRating, Long> {
    List<UserRating> findBySenderIdAndRecipientId(Long senderId, Long recipientId);

    // Простите...
    // Нужно, чтобы те качества,
    // которые еще не получили ни одной оценки, тоже присылались, но с нулем
    @Query(value = """
            SELECT
                                          pv.rating_type as ratingType,
                                          count(ur.rating_type) as count
                                      FROM
                                          (SELECT * FROM (VALUES
                                                              ('POLITENESS'),
                                                              ('LEARNING_ABILITY'),
                                                              ('RESPONSIBILITY'),
                                                              ('CREATIVITY')) AS predefined_values(rating_type)) as "pv"
                                              LEFt JOIN user_rating ur ON ur.rating_type = pv.rating_type
                                              AND ur.recipient_id = :recipientId
                                      GROUP BY pv.rating_type;
            """, nativeQuery = true)
    List<UserRatingProjection> findUserRatingProjectionUsingRecipientId(long recipientId);
}
