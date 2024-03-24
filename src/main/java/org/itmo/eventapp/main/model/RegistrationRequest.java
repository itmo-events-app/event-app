package org.itmo.eventapp.main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Date;

@Entity
@Table(name = "registration_request")
@Getter
@Setter
public class RegistrationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RegistrationRequestStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent_time")
    private Date sentTime;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String email, String passwordHash, String name, String surname, RegistrationRequestStatus status, Date sentTime) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.name = name;
        this.surname = surname;
        this.status = status;
        this.sentTime = sentTime;
    }
}
