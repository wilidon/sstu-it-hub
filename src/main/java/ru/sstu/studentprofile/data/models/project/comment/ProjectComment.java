package ru.sstu.studentprofile.data.models.project.comment;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_comment")
@Getter
@Setter
public class ProjectComment {
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

    @Column(name = "text", nullable = false, length = 1024)
    private String text;

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    private LocalDateTime updatedAt = createdAt;

    @Transient
    private boolean isEdited;

    @PostLoad
    @PostUpdate
    public void setIsEditedPostLoad() {
        isEdited = !createdAt.equals(updatedAt);
    }

}
