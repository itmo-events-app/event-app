package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record NotificationSettingsRequest(
    @NotNull(message = "Поле enableEmail не может быть null!")
    @Schema(example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    boolean enableEmail,

    @NotNull(message = "Поле enablePush не может быть null!")
    @Schema(example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    boolean enablePush
) {
}
