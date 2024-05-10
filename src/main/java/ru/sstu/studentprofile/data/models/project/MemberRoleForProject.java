package ru.sstu.studentprofile.data.models.project;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.sstu.studentprofile.data.models.user.User;

@Entity
@Table(name = "project_member_role_for_project")
@Getter
@Setter
public class MemberRoleForProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "project_member_id", nullable = false)
    private ProjectMember projectMember;

    @ManyToOne
    @JoinColumn(name = "role_for_project_id", nullable = false)
    private RoleForProject role;
}
