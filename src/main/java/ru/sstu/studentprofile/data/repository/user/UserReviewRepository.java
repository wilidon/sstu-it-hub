package ru.sstu.studentprofile.data.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.sstu.studentprofile.data.models.user.UserReview;
import ru.sstu.studentprofile.domain.service.user.dto.UserReviewOut;

public interface UserReviewRepository extends JpaRepository<UserReview, Long> {
    Page<UserReview> findAllByRecipientId(@Param("recipient") long recipientId,
                                          Pageable pageable);
}
