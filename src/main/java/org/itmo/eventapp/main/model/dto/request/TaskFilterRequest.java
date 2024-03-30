package org.itmo.eventapp.main.model.dto.request;

import org.itmo.eventapp.main.model.entity.enums.TaskStatus;

import java.time.LocalDateTime;

public record TaskFilterRequest(Integer assigneeId,
                            Integer assignerId,
                            TaskStatus taskStatus,
                            LocalDateTime deadline) {
}
