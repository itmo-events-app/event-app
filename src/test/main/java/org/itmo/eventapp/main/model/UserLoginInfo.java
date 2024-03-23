package org.itmo.eventapp.main.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.Date;

@Converter(autoApply = true)
class EmailStatusConverter implements AttributeConverter<EmailStatus, String> {
    @Override
    public String convertToDatabaseColumn(EmailStatus color) {
        if (color == null) {
            return null;
        }
        return color.name().toLowerCase();
    }

    @Override
    public EmailStatus convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return EmailStatus.valueOf(value.toUpperCase());
    }
}

@Entity
@Table(name = "user_login_info")
public class UserLoginInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "email_status", name = "email_status")
    @Convert(converter = EmailStatusConverter.class)
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
    private Date lastLoginDate;

    public UserLoginInfo() {
    }

    public UserLoginInfo(User user, String email, EmailStatus emailStatus, String passwordHash, String resetToken, RegistrationRequest registration, Date lastLoginDate) {
        this.user = user;
        this.email = email;
        this.emailStatus = emailStatus;
        this.passwordHash = passwordHash;
        this.resetToken = resetToken;
        this.registration = registration;
        this.lastLoginDate = lastLoginDate;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EmailStatus getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(EmailStatus emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public RegistrationRequest getRegistration() {
        return registration;
    }

    public void setRegistration(RegistrationRequest registration) {
        this.registration = registration;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
}
