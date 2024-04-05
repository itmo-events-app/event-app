package org.itmo.eventapp.main.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.itmo.eventapp.main.model.entity.enums.TaskStatus;

import java.time.LocalDateTime;

public record TaskRequest(
        @NotNull
        @Min(value = 1, message = "Поле eventId не может быть меньше 1!")
        Integer eventId,
        UserShortDataRequest assignee,
        @NotBlank(message = "Поле title не может быть пустым!")
        String title,
        @NotNull(message = "Поле description не может быть null!")
        String description,
        TaskStatus taskStatus,
        PlaceShortDataRequest place,
        @NotNull(message = "Поле deadline не может быть null!")
        LocalDateTime deadline,
        @NotNull(message = "Поле notificationDeadline не может быть null!")
        LocalDateTime notificationDeadline
) {
}
