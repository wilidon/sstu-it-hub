package ru.sstu.studentprofile.data.repository.project;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.project.ProjectMember;
import ru.sstu.studentprofile.data.models.user.User;

import java.util.List;
@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    @Query("SELECT m FROM ProjectMember m WHERE m.project.id = :projectId")
    List<ProjectMember> findMembersUniqueByProjectId(@Param("projectId") long projectId);
}

