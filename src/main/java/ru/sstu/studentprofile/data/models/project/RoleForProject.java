package ru.sstu.studentprofile.data.models.project;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="role_for_project")
@Getter
@Setter
public class RoleForProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name="name", nullable = false, length = 128)
    private String name;

    @OneToMany(mappedBy = "role", orphanRemoval = true)
    private Set<Team> roleTeams = new LinkedHashSet<>();
}
