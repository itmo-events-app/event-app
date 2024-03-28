package org.itmo.eventapp.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.TaskDto;
import org.itmo.eventapp.main.model.dto.TaskFilterDto;
import org.itmo.eventapp.main.model.entity.TaskStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/tasks")
public class TaskController {

    @PostMapping
    public ResponseEntity<Integer> taskAdd(@Valid @RequestBody TaskDto taskDto) {
        Integer taskId = 0; // add task
        // ASSIGNER (USER) ID SHOULD BE TAKEN FROM CONTEXT??? or dto
        // returns id of created task
        // schedule task deadline notification
        return ResponseEntity.ok().body(taskId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> taskGet(@Min(1) @PathVariable Integer id) {
        TaskDto task = null; // get task
        return ResponseEntity.ok().body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> taskEdit(@Min(1) @PathVariable Integer id,
                                      @Valid @RequestBody TaskDto taskDto) {
        // edit task
        // schedule task deadline notification
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> taskDelete(@Min(1) @PathVariable Integer id) {
        // delete task
        // delete task deadline notification
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/set-assignee/{userId}")
    public ResponseEntity<TaskDto> taskSetAssignee(@Min(1) @PathVariable Integer id,
                                                   @Min(1) @PathVariable Integer userId) {
        // set assignee - ORG only
        // schedule task deadline notification
        // delete task deadline notification for prev assignee
        TaskDto updatedTask = null;
        return ResponseEntity.ok().body(updatedTask);
    }

    @PutMapping("/{id}/take-on")
    public ResponseEntity<TaskDto> taskTakeOn(@Min(1) @PathVariable Integer id) {
        // ASSIGNEE (USER) ID SHOULD BE TAKEN FROM CONTEXT???
        // schedule task deadline notification
        TaskDto updatedTask = null;
        return ResponseEntity.ok().body(updatedTask);
    }

    @PutMapping("/{id}/delete-assignee")
    public ResponseEntity<?> taskDeleteAssignee(@Min(1) @PathVariable Integer id) {
        // delete task deadline notification
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/set-status")
    public ResponseEntity<?> taskSetStatus(@Min(1) @PathVariable Integer id,
                                           @NotNull @RequestBody TaskStatus newStatus) {
        // set status - ORG only
        // delete task deadline notification - if done ???
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/mark-done")
    public ResponseEntity<?> taskMarkDone(@Min(1) @PathVariable Integer id) {
        // set status "done"
        // delete task deadline notification - ???
        return ResponseEntity.ok().build();
    }


    @PutMapping("/move/{srcEventId}/{dstEventId}")
    public ResponseEntity<?> taskListMove(@Min(1) @PathVariable Integer srcEventId,
                                          @Min(1) @PathVariable Integer dstEventId,
                                          @NotEmpty @RequestBody List<Integer> taskIds) {
        // change srcEventId to dstEventId in tasks
        // src == dst - ?
        return ResponseEntity.ok().build();
    }

    @PostMapping("/copy/{srcEventId}/{dstEventId}")
    public ResponseEntity<List<TaskDto>> taskListCopy(@Min(1) @PathVariable Integer srcEventId,
                                                      @Min(1) @PathVariable Integer dstEventId,
                                                      @NotEmpty @RequestBody List<Integer> taskIds) {
        List<TaskDto> newTasks = new ArrayList<>();
        // create new tasks in event with dstEventId
        // assignee -> null; status -> new
        // schedule task deadline notification
        // src == dst - ?
        return ResponseEntity.ok().body(newTasks);
    }

    @GetMapping("/show/{eventId}")
    public ResponseEntity<List<TaskDto>> taskListShowInEvent(@Min(1) @PathVariable Integer eventId,
                                                             @Valid @RequestBody TaskFilterDto filter) {
        List<TaskDto> eventTasks = new ArrayList<>();
        // apply filtering in db - ???
        return ResponseEntity.ok().body(eventTasks);
    }

    @GetMapping("/show/where-assignee/{eventId}")
    public ResponseEntity<List<TaskDto>> taskListShowInEventWhereAssignee(@Min(1) @PathVariable Integer eventId) {
        List<TaskDto> eventUserTasks = new ArrayList<>();
        // ASSIGNEE (USER) ID SHOULD BE TAKEN FROM CONTEXT???
        return ResponseEntity.ok().body(eventUserTasks);
    }

    @GetMapping("/show/where-assignee/all")
    public ResponseEntity<List<TaskDto>> taskListShowWhereAssignee() {
        List<TaskDto> userTasks = new ArrayList<>();
        // ASSIGNEE (USER) ID SHOULD BE TAKEN FROM CONTEXT???
        return ResponseEntity.ok().body(userTasks);
    }


}
