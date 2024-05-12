package ru.sstu.studentprofile.data.models.request;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.project.ProjectStatus;
import ru.sstu.studentprofile.data.models.user.User;

@Entity
@Table(name = "request")
@Getter
@Setter
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name="type", nullable = false)
    private RequestType type;

    @Enumerated(EnumType.STRING)
    @Column(name="result", nullable = false)
    private RequestResult result;
}
