package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.itmo.eventapp.main.model.entity.enums.TaskStatus;
import org.itmo.eventapp.main.model.validation.annotation.StartDateBeforeEndDate;

import java.time.LocalDateTime;

import static org.itmo.eventapp.main.exceptionhandling.ExceptionConst.TASK_NOTIFICATION_TO_DEADLINE_VALIDATION;

@StartDateBeforeEndDate(first = "reminder", second = "deadline", message = TASK_NOTIFICATION_TO_DEADLINE_VALIDATION)
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
    @Future(message = "Нельзя назначить крайний срок выполнения задачи в прошлое")
    @NotNull(message = "Поле deadline не может быть null!")
    LocalDateTime deadline,
    @Future(message = "Нельзя назначить уведомление об окончании задачи в прошлое")
    @NotNull(message = "Поле reminder не может быть null!")
    LocalDateTime reminder
) {
}
