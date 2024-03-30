package org.itmo.eventapp.main.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "user_login_info")
@Getter
@Setter
public class UserLoginInfo implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private String email;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "email_status")
    private EmailStatus emailStatus;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "resettoken")
    private String resetToken;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "registration_id")
    private RegistrationRequest registration;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<GrantedAuthority>();
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
