package org.itmo.eventapp.main.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.Date;

@Converter(autoApply = true)
class RegistrationRequestStatusConverter implements AttributeConverter<RegistrationRequestStatus, String> {
    @Override
    public String convertToDatabaseColumn(RegistrationRequestStatus color) {
        if (color == null) {
            return null;
        }
        return color.name().toLowerCase();
    }

    @Override
    public RegistrationRequestStatus convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return RegistrationRequestStatus.valueOf(value.toUpperCase());
    }
}

@Entity
@Table(name = "registration_request")
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
    @Column(columnDefinition = "request_status")
    @Convert(converter = RegistrationRequestStatusConverter.class)
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

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public RegistrationRequestStatus getStatus() {
        return status;
    }

    public void setStatus(RegistrationRequestStatus status) {
        this.status = status;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }
}
