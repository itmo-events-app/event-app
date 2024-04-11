package org.itmo.eventapp.main.model.mapper;

import org.itmo.eventapp.main.model.dto.response.TaskObjectResponse;
import org.itmo.eventapp.main.model.entity.TaskObject;

import java.util.List;

public class TaskObjectMapper {

    private TaskObjectMapper() {
    }

    public static TaskObjectResponse taskObjectToResponse(TaskObject taskObject) {
        return new TaskObjectResponse(
                taskObject.getId(),
                (taskObject.getTask() == null) ? null : taskObject.getTask().getId(),
                taskObject.getOriginalFilename()
        );
    }

    public static List<TaskObjectResponse> taskObjectsToResponseList(List<TaskObject> taskObjects) {
        return taskObjects.stream()
                .map(TaskObjectMapper::taskObjectToResponse)
                .toList();
    }
}
