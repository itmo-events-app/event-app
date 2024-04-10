package org.itmo.eventapp.main.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RecoveryPasswordRequest(

        @NotBlank
        String returnUrl,

        @NotBlank(message = "Поле не может быть пустым")
        String email
) {}
