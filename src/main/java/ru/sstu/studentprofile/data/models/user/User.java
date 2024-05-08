package ru.sstu.studentprofile.data.models.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

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

    @Column(length = 128, nullable = false)
    @Size(max = 128)
    @NotBlank
    private String password;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<UserRole> userRoles = new LinkedHashSet<>();

    private LocalDateTime lastActivityTime = LocalDateTime.now();

    private LocalDateTime createdAt = LocalDateTime.now();

}
