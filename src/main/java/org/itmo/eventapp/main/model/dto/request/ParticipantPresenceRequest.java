package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParticipantPresenceRequest (
        @Min(value = 1, message = "Поле participantId не может быть меньше 1!")
        @NotNull(message = "Поле participantId не может быть null!")
        @Schema(example = "67", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer participantId,
        @NotBlank(message = "Поле isVisited не может быть пустым!")
        @Schema(example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        boolean isVisited
){
}
