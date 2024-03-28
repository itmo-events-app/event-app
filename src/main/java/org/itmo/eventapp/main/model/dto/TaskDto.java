package org.itmo.eventapp.main.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.itmo.eventapp.main.model.entity.TaskStatus;

import java.time.LocalDateTime;

public record TaskDto(Integer id,
                      @Min(1)
                      Integer eventId, // validate not null and > 0
                      UserShortDataDto assignee,
                      @NotNull
                      UserShortDataDto assigner, // validate not null
                      @NotBlank
                      String title, // validate not blank
                      @NotNull
                      String description,
                      @NotNull
                      TaskStatus taskStatus,
                      PlaceShortDataDto place,
                      @NotNull
                      LocalDateTime deadline,
                      @NotNull
                      LocalDateTime notificationDeadline) {
}
