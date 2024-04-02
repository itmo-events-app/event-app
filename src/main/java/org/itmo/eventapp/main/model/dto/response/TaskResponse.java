package org.itmo.eventapp.main.model.dto.response;

import org.itmo.eventapp.main.model.entity.enums.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponse(
        Integer id,
        String title,
        String description,
        TaskStatus taskStatus,
        LocalDateTime creationTime,
        LocalDateTime deadline,
        LocalDateTime notificationDeadline
) {
}