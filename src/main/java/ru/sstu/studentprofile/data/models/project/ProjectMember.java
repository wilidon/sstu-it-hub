package ru.sstu.studentprofile.data.models.project;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.sstu.studentprofile.data.models.user.User;

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
}

// НОВАЯ ТАБЛИЦА МНОГИЕ КО МНОГИЕ СОСТАВНУЮ КЛЮЧ TEAM USER