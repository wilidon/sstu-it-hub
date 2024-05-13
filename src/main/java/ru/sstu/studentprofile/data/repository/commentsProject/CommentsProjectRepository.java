package ru.sstu.studentprofile.data.repository.commentsProject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sstu.studentprofile.data.models.commentsProject.CommentsProject;

import java.util.List;

public interface CommentsProjectRepository extends JpaRepository<CommentsProject, Long> {
    @Query("SELECT c FROM CommentsProject c WHERE c.project.id = :projectId ORDER BY c.createDate DESC")
    Page<CommentsProject> getCommentsProjectByProject(Pageable pageable, long projectId);
}
