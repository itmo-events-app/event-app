package org.itmo.eventapp.main.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.itmo.eventapp.main.model.entity.enums.EmailStatus;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserLoginInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private EmailStatus emailStatus;

    private String passwordHash;

    private String resetToken;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "registration_id")
    private RegistrationRequest registration;

    private LocalDateTime lastLoginDate;
}
