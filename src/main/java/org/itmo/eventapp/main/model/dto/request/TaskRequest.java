package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.itmo.eventapp.main.model.entity.enums.TaskStatus;

import java.time.LocalDateTime;

public record TaskRequest(
    @NotNull
    @Min(value = 1, message = "Поле eventId не может быть меньше 1!")
    @Schema(example = "1")
    Integer eventId,
    @Min(value = 1, message = "Поле assigneeId не может быть меньше 1!")
    @Schema(example = "1")
    Integer assigneeId,
    @NotBlank(message = "Поле title не может быть пустым!")
    @Schema(example = "Настроить проектор")
    String title,
    @NotNull(message = "Поле description не может быть null!")
    @Schema(example = "Настроить проектор в коворкинге")
    String description,
    TaskStatus taskStatus,
    @Min(value = 1, message = "Поле placeId не может быть меньше 1!")
    @Schema(example = "1")
    Integer placeId,
    @FutureOrPresent
    @NotNull(message = "Поле deadline не может быть null!")
    LocalDateTime deadline,
    @Future
    @NotNull(message = "Поле reminder не может быть null!")
    LocalDateTime reminder
) {
}
