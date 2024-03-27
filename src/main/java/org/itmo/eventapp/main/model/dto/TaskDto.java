package org.itmo.eventapp.main.model.dto;

import org.itmo.eventapp.main.model.entity.TaskStatus;

import java.time.LocalDateTime;

public record TaskDto(Integer id,
               Integer eventId, // validate not null
               UserShortDataDto assignee,
               UserShortDataDto assigner, // validate not null
               String title, // validate not blank
               String description,
               TaskStatus taskStatus,
               PlaceShortDataDto place,
               LocalDateTime deadline,
               LocalDateTime notificationDeadline) {
}
