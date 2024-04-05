package org.itmo.eventapp.main.model.dto.response;

import org.itmo.eventapp.main.model.dto.request.PlaceShortDataRequest;
import org.itmo.eventapp.main.model.entity.enums.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponse(
        Integer id,
        Integer eventId,
        String title,
        String description,
        TaskStatus taskStatus,
        UserShortDataResponse assignee,
        PlaceShortDataResponse place,
        LocalDateTime creationTime,
        LocalDateTime deadline,
        LocalDateTime notificationDeadline
) {
}