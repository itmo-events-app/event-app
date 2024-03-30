package org.itmo.eventapp.main.model.mapper;

import org.itmo.eventapp.main.model.dto.response.TaskResponse;
import org.itmo.eventapp.main.model.entity.Task;

public final class TaskMapper {
    public static TaskResponse taskToTaskResponse(Task task) {
        return new TaskResponse(
                task.getTitle(),
                task.getDescription(),
                task.getTaskStatus(),
                task.getDeadline(),
                task.getNotificationDeadline()
        );
    }
}