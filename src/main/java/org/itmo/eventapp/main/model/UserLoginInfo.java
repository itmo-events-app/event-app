package org.itmo.eventapp.main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "user_login_info")
@Getter
@Setter
public class UserLoginInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_status")
    private EmailStatus emailStatus;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "resettoken")
    private String resetToken;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "registration_id")
    private RegistrationRequest registration;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

    public UserLoginInfo() {
    }

    public UserLoginInfo(User user, String email, EmailStatus emailStatus, String passwordHash, String resetToken, RegistrationRequest registration, LocalDateTime lastLoginDate) {
        this.user = user;
        this.email = email;
        this.emailStatus = emailStatus;
        this.passwordHash = passwordHash;
        this.resetToken = resetToken;
        this.registration = registration;
        this.lastLoginDate = lastLoginDate;
    }
}
