package ru.sstu.studentprofile.data.models.commentsProject;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments_project")
@Getter
@Setter
public class CommentsProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @NotNull
    private User author;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @NotNull
    private Project project;

    @Column(name = "text", nullable = false)
    private String text;

    @NotNull
    private LocalDateTime createDate;

}
