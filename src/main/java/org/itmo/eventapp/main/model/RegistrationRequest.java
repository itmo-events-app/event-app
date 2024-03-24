package org.itmo.eventapp.main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "registration_request")
@Getter
@Setter
public class RegistrationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    private String name;

    private String surname;

    @Enumerated(EnumType.STRING)
    private RegistrationRequestStatus status;

    @Column(name = "sent_time")
    private LocalDateTime sentTime;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String email, String passwordHash, String name, String surname, RegistrationRequestStatus status, LocalDateTime sentTime) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.name = name;
        this.surname = surname;
        this.status = status;
        this.sentTime = sentTime;
    }
}
