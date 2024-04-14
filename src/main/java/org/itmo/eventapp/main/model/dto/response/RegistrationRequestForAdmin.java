package org.itmo.eventapp.main.model.dto.response;

import org.itmo.eventapp.main.model.entity.enums.RegistrationRequestStatus;

import java.time.LocalDateTime;

public record RegistrationRequestForAdmin(
    Integer id,
    String email,
    String name,
    String surname,
    RegistrationRequestStatus status,
    LocalDateTime sentTime
) {
}
