package org.itmo.eventapp.main.model.dto.response;

import org.itmo.eventapp.main.model.entity.enums.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponse(
        String title,
        String description,
        TaskStatus taskStatus,
        LocalDateTime deadline,
        LocalDateTime notificationDeadline
) {
}