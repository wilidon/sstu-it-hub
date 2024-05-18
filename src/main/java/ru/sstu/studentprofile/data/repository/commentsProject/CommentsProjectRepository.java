package ru.sstu.studentprofile.data.repository.commentsProject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sstu.studentprofile.data.models.commentsProject.ProjectComment;

public interface CommentsProjectRepository extends JpaRepository<ProjectComment, Long> {
    @Query("SELECT c FROM ProjectComment c WHERE c.project.id = :projectId")
    Page<ProjectComment> getCommentsProjectByProject(long projectId, Pageable pageable);
}
