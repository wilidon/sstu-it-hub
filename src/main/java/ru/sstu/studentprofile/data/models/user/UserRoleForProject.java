package ru.sstu.studentprofile.data.models.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.sstu.studentprofile.data.models.project.RoleForProject;

@Entity
@Table(name = "user_role_for_project")
@Getter
@Setter
public class UserRoleForProject {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_for_project_id", nullable = false)
    private RoleForProject role;
}
