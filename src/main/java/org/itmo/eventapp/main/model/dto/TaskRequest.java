package org.itmo.eventapp.main.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.itmo.eventapp.main.model.entity.TaskStatus;

import java.time.LocalDateTime;

public record TaskRequest(Integer id,
                      @Min(value = 1, message = "Поле eventId не может быть меньше 1!")
                      Integer eventId, // validate not null and > 0
                      UserShortDataRequest assignee,
                      @NotNull(message = "Поле assigner не может быть null!")
                      UserShortDataRequest assigner, // validate not null
                      @NotBlank(message = "Поле title не может быть пустым!")
                      String title, // validate not blank
                      @NotNull(message = "Поле description не может быть null!")
                      String description,
                      TaskStatus taskStatus,
                      PlaceShortDataRequest place,
                      @NotNull(message = "Поле deadline не может быть null!")
                      LocalDateTime deadline,
                      @NotNull(message = "Поле notificationDeadline не может быть null!")
                      LocalDateTime notificationDeadline) {
}
