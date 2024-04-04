package org.itmo.eventapp.main.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record NotificationRequest(
        @NotNull
        @Min(value = 1, message = "Поле Id не может быть меньше 1!")
        Integer id
) {
}
