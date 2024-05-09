package ru.sstu.studentprofile.data.repository.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sstu.studentprofile.data.models.project.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
