package org.itmo.eventapp.main.model.dto;

import java.io.Serializable;

public record TaskNotificationDTO(
        String taskName,
        String taskAssignerName,
        String taskAssignerEmail,
        String taskAssigneeName,
        String taskAssigneeEmail,
        String taskEventName,
        String taskStatus
) {
}
