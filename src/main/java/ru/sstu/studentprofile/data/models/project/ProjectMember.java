package ru.sstu.studentprofile.data.models.project;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.data.models.user.UserRole;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "project_member")
@Getter
@Setter
public class ProjectMember {
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

    @OneToMany(mappedBy = "projectMember", orphanRemoval = true)
    private Set<MemberRoleForProject> projectMemberRoles = new LinkedHashSet<>();
}

