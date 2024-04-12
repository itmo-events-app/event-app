package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RecoveryPasswordRequest(

        @NotBlank
        @Schema(example = "https:/localhost:1111/", requiredMode = Schema.RequiredMode.REQUIRED)
        String returnUrl,

        @NotBlank(message = "Поле не может быть пустым")
        @Schema(example = "333666@niuitmo.ru", requiredMode = Schema.RequiredMode.REQUIRED)
        String email
) {}
