package ru.sstu.studentprofile.data.repository.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sstu.studentprofile.data.models.project.ProjectMember;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    @Query("""
            select case when count(pm) > 0
             then true else false end
             from ProjectMember pm
            where pm.project.id in (
                select p.id from Project p
                join ProjectMember pm on p.id = pm.project.id
                where pm.user.id = :senderId
            ) and pm.user.id = :recipientId
            """
    )
    boolean existsByRecipientIdAndSenderId(long recipientId, long senderId);
}