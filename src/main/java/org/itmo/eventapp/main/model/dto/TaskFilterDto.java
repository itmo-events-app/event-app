package org.itmo.eventapp.main.model.dto;

import org.itmo.eventapp.main.model.entity.TaskStatus;

import java.time.LocalDateTime;

public record TaskFilterDto(Integer assigneeId,
                            Integer assignerId,
                            TaskStatus taskStatus,
                            LocalDateTime deadline) {
}
