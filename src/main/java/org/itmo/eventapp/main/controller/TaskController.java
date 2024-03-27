package org.itmo.eventapp.main.controller;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.TaskDto;
import org.itmo.eventapp.main.model.entity.TaskStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/tasks")
public class TaskController {

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> taskGet(@PathVariable Integer id) {
        TaskDto task = null; // get task
        return ResponseEntity.ok().body(task);
    }

    // GET EVENT ID IN PRE-AUTHORIZE ???
    @PostMapping("/add")
    public ResponseEntity<Integer> taskAdd(@RequestBody TaskDto taskDto) {
        Integer taskId = 0; // add task
        // ASSIGNER (USER) ID SHOULD BE TAKEN FROM CONTEXT??? or dto
        // returns id of created task
        // schedule task deadline notification
        return ResponseEntity.ok().body(taskId);
    }

    @PostMapping("/move/{srcEventId}/{dstEventId}")
    public ResponseEntity<?> taskListMove(@PathVariable Integer srcEventId,
                                          @PathVariable Integer dstEventId,
                                          @RequestBody List<Integer> taskIds) {
        // change srcEventId to dstEventId in tasks
        // src == dst - ?
        return ResponseEntity.ok().build();
    }

    @PostMapping("/copy/{srcEventId}/{dstEventId}")
    public ResponseEntity<List<TaskDto>> taskListCopy(@PathVariable Integer srcEventId,
                                                      @PathVariable Integer dstEventId,
                                                      @RequestBody List<Integer> taskIds) {
        List<TaskDto> newTasks = new ArrayList<>();
        // create new tasks in event with dstEventId
        // assignee -> null; status -> new
        // schedule task deadline notification
        // src == dst - ?
        return ResponseEntity.ok().body(newTasks);
    }

    @PostMapping("/{id}/edit")
    public ResponseEntity<?> taskEdit(@PathVariable Integer id,
                                      @RequestBody TaskDto taskDto) {
        // edit task
        // schedule task deadline notification
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<?> taskDelete(@PathVariable Integer id) {
        // delete task
        // delete task deadline notification
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{id}/set-assignee/{userId}")
    public ResponseEntity<TaskDto> taskSetAssignee(@PathVariable Integer id,
                                                   @PathVariable Integer userId) {
        // set assignee - ORG only
        // schedule task deadline notification
        // delete task deadline notification for prev assignee
        TaskDto updatedTask = null;
        return ResponseEntity.ok().body(updatedTask);
    }

    //    @PostMapping("/{id}/take-on/{userId}")
    @PostMapping("/{id}/take-on")
    public ResponseEntity<TaskDto> taskTakeOn(@PathVariable Integer id
//                                              @PathVariable Integer userId
    ) {
        // ASSIGNEE (USER) ID SHOULD BE TAKEN FROM CONTEXT???
        // schedule task deadline notification
        TaskDto updatedTask = null;
        return ResponseEntity.ok().body(updatedTask);
    }

    @PostMapping("/{id}/delete-assignee")
    public ResponseEntity<?> taskDeleteAssignee(@PathVariable Integer id) {
        // delete task deadline notification
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/set-status")
    public ResponseEntity<?> taskSetStatus(@PathVariable Integer id,
                                           @RequestBody TaskStatus newStatus) {
        // set status - ORG only
        // delete task deadline notification - if done ???
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/mark-done")
    public ResponseEntity<?> taskMarkDone(@PathVariable Integer id) {
        // set status "done"
        // delete task deadline notification - ???
        return ResponseEntity.ok().build();
    }


    @GetMapping("/show/{eventId}/all")
    public ResponseEntity<List<TaskDto>> taskListShowAllLocal(@PathVariable Integer eventId) {
        List<TaskDto> eventTasks = new ArrayList<>();
        return ResponseEntity.ok().body(eventTasks);
    }

    @GetMapping("/show/{eventId}/where-assignee")
    public ResponseEntity<List<TaskDto>> taskListShowWhereAssigneeLocal(@PathVariable Integer eventId) {
        List<TaskDto> eventUserTasks = new ArrayList<>();
        // ASSIGNEE (USER) ID SHOULD BE TAKEN FROM CONTEXT???
        return ResponseEntity.ok().body(eventUserTasks);
    }

    @GetMapping("/show/all/where-assignee")
    public ResponseEntity<List<TaskDto>> taskListShowWhereAssigneeGlobal() {
        List<TaskDto> userTasks = new ArrayList<>();
        // ASSIGNEE (USER) ID SHOULD BE TAKEN FROM CONTEXT???
        return ResponseEntity.ok().body(userTasks);
    }

    @GetMapping("/show/{eventId}/where-user-assignee/{userId}")
    public ResponseEntity<List<TaskDto>> taskListShowWhereUserAssigneeLocal(@PathVariable Integer eventId,
                                                                @PathVariable Integer userId) {
        List<TaskDto> eventUserTasks = new ArrayList<>();
        // ASSIGNEE (USER) ID SHOULD BE TAKEN FROM CONTEXT???
        return ResponseEntity.ok().body(eventUserTasks);
    }

}
