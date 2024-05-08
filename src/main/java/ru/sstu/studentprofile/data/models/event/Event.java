package ru.sstu.studentprofile.data.models.event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.sstu.studentprofile.data.models.user.User;

import java.time.LocalDateTime;

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
}
