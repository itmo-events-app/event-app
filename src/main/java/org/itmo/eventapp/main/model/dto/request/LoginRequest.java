package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;


public record LoginRequest(
    @NotBlank(message = "Поле обязательно для заполнения")
    @Schema(example = "333666@niuitmo.ru", requiredMode = Schema.RequiredMode.REQUIRED)
    String login,

    @NotBlank(message = "Поле обязательно для заполнения")
    @Schema(example = "PaSsWoRd1!", requiredMode = Schema.RequiredMode.REQUIRED)
    String password
) {
}
