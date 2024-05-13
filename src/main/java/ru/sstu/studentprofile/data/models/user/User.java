package ru.sstu.studentprofile.data.models.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.project.ProjectMember;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "\"user\"",
        indexes = @Index(columnList = "login", name = "user_email"))
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, length = 64, nullable = false)
    @Size(max = 128)
    @NotBlank
    private String login;

    @Column(unique = true, length = 128, nullable = false)
    @Email
    @Size(max = 128)
    @NotBlank
    private String email;

    @Column(name = "avatar", length = 512)
    private String avatar;

    @Column(name = "background", length = 512)
    private String background;

    @Column(length = 128, nullable = false)
    @NotBlank
    private String lastName; // Фамилия
    @NotBlank
    @Column(length = 128, nullable = false)
    private String firstName; // Имя
    @Column(length = 128, nullable = true)
    private String middleName; // Отчество

    @Column(length = 128, nullable = false)
    @Size(max = 128)
    @NotBlank
    private String password;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<UserRole> userRoles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "leader", orphanRemoval = true)
    private Set<Project> userLeaders = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<ProjectMember> userProjectMembers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<UserRoleForProject> userRolesForProject = new LinkedHashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserMedia userMedia;

    private LocalDateTime lastActivityTime = LocalDateTime.now();

    private LocalDateTime createdAt = LocalDateTime.now();

}
