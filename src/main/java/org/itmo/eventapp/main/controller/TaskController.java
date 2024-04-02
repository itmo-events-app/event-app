package org.itmo.eventapp.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.TaskFilterRequest;
import org.itmo.eventapp.main.model.dto.request.TaskRequest;
import org.itmo.eventapp.main.model.dto.response.TaskResponse;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.model.entity.enums.TaskStatus;
import org.itmo.eventapp.main.model.mapper.TaskMapper;
import org.itmo.eventapp.main.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/tasks")
public class TaskController {
    private final TaskService taskService;

    // TODO: Add TaskService and move request processing there

    @PostMapping
    public ResponseEntity<Integer> taskAdd(@Valid @RequestBody TaskRequest taskRequest) {
        TaskResponse task = taskService.save(taskRequest);
        return ResponseEntity.status(201).body(task.id());
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
        TaskResponse edited = taskService.edit(id, taskRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> taskDelete(@Min(value = 1, message = "Параметр id не может быть меньше 1!")
                                        @PathVariable Integer id) {
        // delete task
        taskService.delete(id);
        // delete task deadline notification
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/assignee/{userId}")
    public ResponseEntity<TaskResponse> taskSetAssignee(
            @Min(value = 1, message = "Параметр id не может быть меньше 1!")
            @PathVariable Integer id,
            @Min(value = 1, message = "Параметр userId не может быть меньше 1!")
            @PathVariable Integer userId
    ) {
        TaskResponse updatedTask = taskService.setAssignee(id, userId);
        return ResponseEntity.ok().body(updatedTask);
    }

    @PutMapping("/{id}/assignee")
    public ResponseEntity<TaskResponse> taskTakeOn(
            @Min(value = 1, message = "Параметр id не может быть меньше 1!")
            @PathVariable Integer id
    ) {
        Integer userId = 0; // TODO: get from principal
        TaskResponse updatedTask = taskService.setAssignee(id, userId);
        return ResponseEntity.ok().body(updatedTask);
    }

    // p35 && also delete yourself as privilege 41
    @DeleteMapping("/{id}/assignee")
    public ResponseEntity<TaskResponse> taskDeleteAssignee(
            @Min(value = 1, message = "Параметр id не может быть меньше 1!")
            @PathVariable Integer id
    ) {
        TaskResponse updatedTask = taskService.setAssignee(id, -1);
        return ResponseEntity.ok().body(updatedTask);
    }

    //privilege 32 && privilege 39
    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponse> taskSetStatus(
            @Min(value = 1, message = "Параметр id не может быть меньше 1!")
            @PathVariable Integer id,
            @NotNull(message = "Параметр newStatus не может быть null!")
            @RequestBody TaskStatus newStatus
    ) {
        if (newStatus == TaskStatus.EXPIRED) {
            throw new IllegalArgumentException("Недопустимый для ручного выставления статус!");
        }
        TaskResponse updatedTask = taskService.setStatus(id, newStatus);
        return ResponseEntity.ok().body(updatedTask);
    }

    //privilege 39
//    @PutMapping("/{id}/status/assignee")
//    public ResponseEntity<?> taskSetStatusAsAssignee(
//            @Min(value = 1, message = "Параметр id не может быть меньше 1!")
//            @PathVariable Integer id,
//            @NotNull(message = "Параметр newStatus не может быть null!")
//            @RequestBody TaskStatus newStatus
//    ) {
//        // set status "done"
//        // delete task deadline notification - if done ???
//        return ResponseEntity.ok().build();
//    }


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
        // change srcEventId to dstEventId in tasks
        // src == dst - ?
        return ResponseEntity.ok().build();
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
        List<TaskResponse> newTasks = new ArrayList<>();
        // create new tasks in event with dstEventId
        // assignee -> null; status -> new
        // schedule task deadline notification
        // src == dst - ?
        return ResponseEntity.ok().body(newTasks);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<TaskRequest>> taskListShowInEvent(
            @Min(value = 1, message = "Параметр eventId не может быть меньше 1!")
            @PathVariable Integer eventId,
            @Valid @RequestBody TaskFilterRequest filter
    ) {
        List<TaskRequest> eventTasks = new ArrayList<>();
        // apply filtering in db - ???
        return ResponseEntity.ok().body(eventTasks);
    }

    @GetMapping("/event/{eventId}/where-assignee")
    public ResponseEntity<List<TaskRequest>> taskListShowInEventWhereAssignee(
            @Min(value = 1, message = "Параметр eventId не может быть меньше 1!")
            @PathVariable Integer eventId
    ) {
        List<TaskRequest> eventUserTasks = new ArrayList<>();
        // ASSIGNEE (USER) ID SHOULD BE TAKEN FROM CONTEXT???
        return ResponseEntity.ok().body(eventUserTasks);
    }

    @GetMapping("/where-assignee")
    public ResponseEntity<List<TaskRequest>> taskListShowWhereAssignee() {
        List<TaskRequest> userTasks = new ArrayList<>();
        // ASSIGNEE (USER) ID SHOULD BE TAKEN FROM CONTEXT???
        return ResponseEntity.ok().body(userTasks);
    }


}
