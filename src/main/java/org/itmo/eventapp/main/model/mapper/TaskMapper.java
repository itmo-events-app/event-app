package org.itmo.eventapp.main.model.mapper;
import org.itmo.eventapp.main.minio.MinioService;
import org.itmo.eventapp.main.model.dto.request.TaskRequest;
import org.itmo.eventapp.main.model.dto.response.TaskResponse;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.Place;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.service.TaskService;

import java.util.List;

public final class TaskMapper {

    private TaskMapper() {
    }

    public static TaskResponse taskToTaskResponse(Task task, TaskService taskService) {

        List<String> filenames = taskService.getFileNames(task.getId());

        return new TaskResponse(
                task.getId(),
                EventMapper.eventToEventShortDataResponse(task.getEvent()),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                UserMapper.userToUserShortResponse(task.getAssignee()),
                PlaceMapper.placeToPlaceShortResponse(task.getPlace()),
                task.getCreationTime(),
                task.getDeadline(),
                task.getReminder(),
                filenames
        );
    }

    public static Task taskRequestToTask(
            TaskRequest taskRequest,
            Event event,
            User assignee,
            User assigner,
            Place place
    ) {
        return Task.builder()
                .event(event)
                .assignee(assignee)
                .assigner(assigner)
                .title(taskRequest.title())
                .description(taskRequest.description())
                .status(taskRequest.taskStatus())
                .place(place)
                .deadline(taskRequest.deadline())
                .reminder(taskRequest.reminder())
                .build();
    }


    public static List<TaskResponse> tasksToTaskResponseList(List<Task> tasks, TaskService taskService) {
        return tasks.stream()
                .map((task) -> TaskMapper.taskToTaskResponse(task, taskService))
                .toList();
    }
}