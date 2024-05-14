package ru.sstu.studentprofile.data.models.event;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.data.models.user.UserRole;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "event")
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 512)
    private String avatar;

    @Column(length = 128)
    @NotBlank
    private String name;

    @Column(length = 2048)
    @NotBlank
    private String description;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @NotNull
    private User author;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @OneToMany(mappedBy = "event")
    @OrderBy("place asc")
    private List<EventWinner> winners;

    @OneToMany(mappedBy = "event", orphanRemoval = true)
    private Set<Project> eventProjects = new LinkedHashSet<>();
}
