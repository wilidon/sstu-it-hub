package ru.sstu.studentprofile.data.models.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class UserMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 128)
    private String vkUrl;

    @Column(length = 128)
    private String tgUrl;

    @Column(length = 11)
    private String phone;

    @Column(length = 512)
    private String about;

}
