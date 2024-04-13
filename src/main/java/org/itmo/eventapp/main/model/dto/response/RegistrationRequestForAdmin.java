package org.itmo.eventapp.main.model.dto.response;

import java.time.LocalDateTime;

import org.itmo.eventapp.main.model.entity.enums.RegistrationRequestStatus;

public record RegistrationRequestForAdmin(
    Integer id,
    String email,
    String name,
    String surname,
    RegistrationRequestStatus status,
    LocalDateTime sentTime
) {
}
