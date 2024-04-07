package org.itmo.eventapp.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.TaskRequest;
import org.itmo.eventapp.main.model.dto.response.TaskResponse;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.model.entity.enums.TaskStatus;
import org.itmo.eventapp.main.model.mapper.TaskMapper;
import org.itmo.eventapp.main.service.TaskService;
import org.itmo.eventapp.main.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @PreAuthorize("@taskSecurityExpression.canCreateTask(#taskRequest.eventId)")
    @PostMapping
    public ResponseEntity<Integer> taskAdd(@Valid @RequestBody TaskRequest taskRequest) {
        Task task = taskService.save(taskRequest);
        return ResponseEntity.status(201).body(task.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> taskGet(@Min(value = 1, message = "Параметр id не может быть меньше 1!")
                                                @PathVariable Integer id) {
        Optional<Task> task = taskService.findById(id);

        return task.map(t -> ResponseEntity.ok().body(TaskMapper.taskToTaskResponse(t)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> taskEdit(@Min(value = 1, message = "Параметр id не может быть меньше 1!")
                                                 @PathVariable Integer id,
                                                 @Valid @RequestBody TaskRequest taskRequest) {
        Task edited = taskService.edit(id, taskRequest);
        return ResponseEntity.ok().body(TaskMapper.taskToTaskResponse(edited));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> taskDelete(@Min(value = 1, message = "Параметр id не может быть меньше 1!")
                                        @PathVariable Integer id) {
        // delete task
        taskService.delete(id);
        // delete task deadline notification
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/{id}/assignee/{userId}")
    public ResponseEntity<TaskResponse> taskSetAssignee(
            @Min(value = 1, message = "Параметр id не может быть меньше 1!")
            @PathVariable Integer id,
            @Min(value = 1, message = "Параметр userId не может быть меньше 1!")
            @PathVariable Integer userId
    ) {
        Task updatedTask = taskService.setAssignee(id, userId);
        return ResponseEntity.ok().body(TaskMapper.taskToTaskResponse(updatedTask));
    }

    /*TODO: TEST*/
    @PutMapping("/{id}/assignee")
    public ResponseEntity<TaskResponse> taskTakeOn(
            @Min(value = 1, message = "Параметр id не может быть меньше 1!")
            @PathVariable Integer id
    ) {
        /*TODO: TEST*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Integer userId = userService.findByLogin(currentPrincipalName).getId();

        Task updatedTask = taskService.setAssignee(id, userId);
        return ResponseEntity.ok().body(TaskMapper.taskToTaskResponse(updatedTask));
    }

    // p35 && also delete yourself as privilege 41
    @DeleteMapping("/{id}/assignee")
    public ResponseEntity<TaskResponse> taskDeleteAssignee(
            @Min(value = 1, message = "Параметр id не может быть меньше 1!")
            @PathVariable Integer id
    ) {
        Task updatedTask = taskService.setAssignee(id, -1);
        return ResponseEntity.ok().body(TaskMapper.taskToTaskResponse(updatedTask));
    }

    //privilege 32 && privilege 39
    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponse> taskSetStatus(
            @Min(value = 1, message = "Параметр id не может быть меньше 1!")
            @PathVariable Integer id,
            @NotNull(message = "Параметр newStatus не может быть null!")
            @RequestBody TaskStatus newStatus
    ) {
        Task updatedTask = taskService.setStatus(id, newStatus);
        return ResponseEntity.ok().body(TaskMapper.taskToTaskResponse(updatedTask));
    }


    /*TODO: TEST*/

    //    @PutMapping("/event/{srcEventId}/{dstEventId}")
    @PutMapping("/event/{dstEventId}")
    public ResponseEntity<List<TaskResponse>> taskListMove(
//            @Min(value = 1, message = "Параметр srcEventId не может быть меньше 1!")
//            @PathVariable Integer srcEventId,
            @Min(value = 1, message = "Параметр dstEventId не может быть меньше 1!")
            @PathVariable Integer dstEventId,
            @NotEmpty(message = "Список task id не может быть пустым!")
            @RequestBody List<Integer> taskIds
    ) {
        List<Task> updTasks = taskService.moveTasks(dstEventId, taskIds);
        return ResponseEntity.ok().body(TaskMapper.tasksToTaskResponseList(updTasks));
    }

    //    @PostMapping("/event/{srcEventId}/{dstEventId}")
    @PostMapping("/event/{dstEventId}")
    public ResponseEntity<List<TaskResponse>> taskListCopy(
//            @Min(value = 1, message = "Параметр srcEventId не может быть меньше 1!")
//            @PathVariable Integer srcEventId,
            @Min(value = 1, message = "Параметр dstEventId не может быть меньше 1!")
            @PathVariable Integer dstEventId,
            @NotEmpty(message = "Список task id не может быть пустым!")
            @RequestBody List<Integer> taskIds
    ) {
        List<Task> newTasks = taskService.copyTasks(dstEventId, taskIds);
        // src == dst - ?
        return ResponseEntity.ok().body(TaskMapper.tasksToTaskResponseList(newTasks));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<TaskResponse>> taskListShowInEvent(
            @Min(value = 1, message = "Параметр eventId не может быть меньше 1!")
            @PathVariable Integer eventId,
            @RequestParam(required = false) Integer assigneeId,
            @RequestParam(required = false) Integer assignerId,
            @RequestParam(required = false) TaskStatus taskStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadlineLowerLimit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadlineUpperLimit,
            @RequestParam(required = false, defaultValue = "false") Boolean subEventTasksGet
    ) {
        List<Task> eventTasks =
                taskService.getEventTasksWithFilter(eventId,
                        assigneeId,
                        assignerId,
                        taskStatus,
                        deadlineLowerLimit,
                        deadlineUpperLimit,
                        subEventTasksGet);
        return ResponseEntity.ok().body(TaskMapper.tasksToTaskResponseList(eventTasks));
    }

    @GetMapping("/event/{eventId}/where-assignee")
    public ResponseEntity<List<TaskResponse>> taskListShowInEventWhereAssignee(
            @Min(value = 1, message = "Параметр eventId не может быть меньше 1!")
            @PathVariable Integer eventId,
            @RequestParam(required = false) Integer assignerId,
            @RequestParam(required = false) TaskStatus taskStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadlineLowerLimit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadlineUpperLimit,
            @RequestParam(required = false, defaultValue = "false") Boolean subEventTasksGet
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Integer userId = userService.findByLogin(currentPrincipalName).getId();

        List<Task> eventUserTasks =
                taskService.getEventTasksWithFilter(eventId,
                        userId,
                        assignerId,
                        taskStatus,
                        deadlineLowerLimit,
                        deadlineUpperLimit,
                        subEventTasksGet);

        return ResponseEntity.ok().body(TaskMapper.tasksToTaskResponseList(eventUserTasks));
    }

    @GetMapping("/where-assignee")
    public ResponseEntity<List<TaskResponse>> taskListShowWhereAssignee(
            @RequestParam(required = false) Integer eventId,
            @RequestParam(required = false) Integer assignerId,
            @RequestParam(required = false) TaskStatus taskStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadlineLowerLimit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadlineUpperLimit
            ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Integer userId = userService.findByLogin(currentPrincipalName).getId();

        List<Task> userTasks = taskService.getUserTasksWithFilter(eventId,
                userId,
                assignerId,
                taskStatus,
                deadlineLowerLimit,
                deadlineUpperLimit);
        return ResponseEntity.ok().body(TaskMapper.tasksToTaskResponseList(userTasks));
    }


}
