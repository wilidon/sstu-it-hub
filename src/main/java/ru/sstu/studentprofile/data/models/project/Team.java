package ru.sstu.studentprofile.data.models.project;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.sstu.studentprofile.data.models.user.User;

@Entity
@Table(name = "team")
@Getter
@Setter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_for_project_id", nullable = false)
    private RoleForProject role;
}
