package ru.sstu.studentprofile.data.repository.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sstu.studentprofile.data.models.request.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT r FROM Request r WHERE r.sender.id = :userId OR r.recipient.id = :userId")
    List<Request> getAllByIdUser(long userId);
}
