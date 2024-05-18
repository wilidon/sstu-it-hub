package ru.sstu.studentprofile.data.models.project;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.sstu.studentprofile.data.models.project.comment.ProjectComment;
import ru.sstu.studentprofile.data.models.event.Event;
import ru.sstu.studentprofile.data.models.user.User;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "project")
@Getter
@Setter
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name="avatar", length = 512)
    private String avatar;

    @Column(name="name", length = 128, nullable = false)
    private String name;

    @Column(name="description", length = 2048)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private ProjectStatus status;

    @NotNull
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "leader_id", nullable = false)
    private User leader;

    @OneToOne(cascade = CascadeType.ALL)
    private ProjectMedia projectMedia;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = true)
    private Event event;

    @OneToMany(mappedBy = "project", orphanRemoval = true)
    private Set<ProjectMember> projectMembers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "project", orphanRemoval = true)
    private Set<ActualRoleForProject> actualRoleForProject = new LinkedHashSet<>();

    @OneToMany(mappedBy = "project", orphanRemoval = true)
    private Set<ProjectComment> projectComment = new LinkedHashSet<>();
}
