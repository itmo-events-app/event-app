package org.itmo.eventapp.main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.TaskRequest;
import org.itmo.eventapp.main.model.dto.response.TaskObjectResponse;
import org.itmo.eventapp.main.model.dto.response.TaskResponse;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.model.entity.TaskObject;
import org.itmo.eventapp.main.model.entity.enums.TaskStatus;
import org.itmo.eventapp.main.model.mapper.TaskMapper;
import org.itmo.eventapp.main.model.mapper.TaskObjectMapper;
import org.itmo.eventapp.main.service.TaskService;
import org.itmo.eventapp.main.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @Operation(summary = "Создание задачи")
    @PreAuthorize("@taskSecurityExpression.canCreateTask(#taskRequest.eventId)")
    @PostMapping
    public ResponseEntity<Integer> taskAdd(@Valid @RequestBody TaskRequest taskRequest) {
        Task task = taskService.save(taskRequest);
        return ResponseEntity.status(201).body(task.getId());
    }

    @Operation(summary = "Получение задачи по id")
    @PreAuthorize("@taskSecurityExpression.canGetTask(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> taskGet(@PathVariable @Min(value = 1, message = "Параметр id не может быть меньше 1!")
                                                @Parameter(name = "id", description = "ID задачи", example = "1") Integer id) {
        Task task = taskService.findById(id);

        return ResponseEntity.ok().body(TaskMapper.taskToTaskResponse(task));
    }

    @Operation(summary = "Редактирование задачи")
    @PreAuthorize("@taskSecurityExpression.canEditTask(#taskRequest.eventId)")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> taskEdit(@Min(value = 1, message = "Параметр id не может быть меньше 1!")
                                                 @PathVariable @Parameter(name = "id", description = "ID задачи", example = "1") Integer id,
                                                 @Valid @RequestBody TaskRequest taskRequest) {
        Task edited = taskService.edit(id, taskRequest);
        return ResponseEntity.ok().body(TaskMapper.taskToTaskResponse(edited));
    }

/*
    @Operation(summary = "Добавление файлов к задаче")
    @PreAuthorize("@taskSecurityExpression.canEditTaskFiles(#id)")
    @PutMapping("/{id}/files")
    public ResponseEntity<List<TaskObjectResponse>> uploadFiles(@Min(value = 1, message = "Параметр id не может быть меньше 1!")
                                                                @PathVariable @Parameter(name = "id", description = "ID задачи", example = "1") Integer id,
                                                                @RequestPart List<MultipartFile> files) {

        List<TaskObject> taskObjects = taskService.addFiles(id, files);

        return ResponseEntity.ok().body(TaskObjectMapper.taskObjectsToResponseList(taskObjects));
    }

    @Operation(summary = "Удаление файлов из задачи")
    @PreAuthorize("@taskSecurityExpression.canEditTaskFiles(#id)")
    @DeleteMapping("/{id}/files")
    public ResponseEntity<Void> deleteFiles(@Min(value = 1, message = "Параметр id не может быть меньше 1!")
                                         @PathVariable @Parameter(name = "id", description = "ID задачи", example = "1") Integer id,
                                         @RequestBody List<Integer> fileObjectIds) {

        taskService.deleteFiles(id, fileObjectIds);

        return ResponseEntity.status(204).build();
    }

 */

    @Operation(summary = "Добавление файлов к задаче")
    @PreAuthorize("@taskSecurityExpression.canEditTaskFiles(#id)")
    @PutMapping("/{id}/files")
    public ResponseEntity<List<String>> uploadFiles(@Min(value = 1, message = "Параметр id не может быть меньше 1!")
                                                                @PathVariable @Parameter(name = "id", description = "ID задачи", example = "1") Integer id,
                                                                @RequestPart List<MultipartFile> files) {

        return ResponseEntity.ok().body(taskService.addFiles(id, files));
    }

    @Operation(summary = "Получение списка имен файлов задачи")
    @PreAuthorize("@taskSecurityExpression.canGetTask(#id)")
    @GetMapping("/{id}/files")
    public ResponseEntity<List<String>> getFileNames(@Min(value = 1, message = "Параметр id не может быть меньше 1!")
                                                    @PathVariable @Parameter(name = "id", description = "ID задачи", example = "1") Integer id) {

        return ResponseEntity.ok().body(taskService.getFileNames(id));
    }

    @Operation(summary = "Удаление файлов из задачи")
    @PreAuthorize("@taskSecurityExpression.canEditTaskFiles(#id)")
    @DeleteMapping("/{id}/files")
    public ResponseEntity<Void> deleteFiles(@Min(value = 1, message = "Параметр id не может быть меньше 1!")
                                            @PathVariable @Parameter(name = "id", description = "ID задачи", example = "1") Integer id,
                                            @RequestBody List<String> fileNamesInMinio) {

        taskService.deleteFiles(id, fileNamesInMinio);

        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Удаление задачи")
    @PreAuthorize("@taskSecurityExpression.canDeleteTask(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> taskDelete(@Min(value = 1, message = "Параметр id не может быть меньше 1!")
                                           @PathVariable @Parameter(name = "id", description = "ID задачи", example = "1") Integer id) {
        // delete task
        taskService.delete(id);
        // delete task deadline notification
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Назначение исполнителя задачи")
    @PreAuthorize("@taskSecurityExpression.canEditTaskAssignee(#id)")
    @PutMapping("/{id}/assignee/{userId}")
    public ResponseEntity<TaskResponse> taskSetAssignee(
            @Min(value = 1, message = "Параметр id не может быть меньше 1!")
            @PathVariable @Parameter(name = "id", description = "ID задачи", example = "1") Integer id,
            @Min(value = 1, message = "Параметр userId не может быть меньше 1!")
            @PathVariable @Parameter(name = "userId", description = "ID пользователя", example = "1") Integer userId
    ) {
        Task updatedTask = taskService.setAssignee(id, userId);
        return ResponseEntity.ok().body(TaskMapper.taskToTaskResponse(updatedTask));
    }

    /*TODO: TEST*/
    @Operation(summary = "Назначение себя исполнителем задачи")
    @PreAuthorize("@taskSecurityExpression.canTakeOnTask(#id)")
    @PutMapping("/{id}/assignee")
    public ResponseEntity<TaskResponse> taskTakeOn(
            @Min(value = 1, message = "Параметр id не может быть меньше 1!")
            @PathVariable @Parameter(name = "id", description = "ID задачи", example = "1") Integer id
    ) {
        /*TODO: TEST*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Integer userId = userService.findByLogin(currentPrincipalName).getId();

        Task updatedTask = taskService.setAssignee(id, userId);
        return ResponseEntity.ok().body(TaskMapper.taskToTaskResponse(updatedTask));
    }

    // p35 && also delete yourself as privilege 41
    @Operation(summary = "Удаление исполнителя задачи")
    @PreAuthorize("@taskSecurityExpression.canDeleteTaskAssignee(#id)")
    @DeleteMapping("/{id}/assignee")
    public ResponseEntity<TaskResponse> taskDeleteAssignee(
            @Min(value = 1, message = "Параметр id не может быть меньше 1!")
            @PathVariable @Parameter(name = "id", description = "ID задачи", example = "1") Integer id
    ) {
        Task updatedTask = taskService.setAssignee(id, -1);
        return ResponseEntity.ok().body(TaskMapper.taskToTaskResponse(updatedTask));
    }

    //privilege 32 && privilege 39
    @Operation(summary = "Установка статуса задачи")
    @PreAuthorize("@taskSecurityExpression.canEditTaskStatus(#id)")
    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponse> taskSetStatus(
            @Min(value = 1, message = "Параметр id не может быть меньше 1!")
            @PathVariable @Parameter(name = "id", description = "ID задачи", example = "1") Integer id,
            @NotNull(message = "Параметр newStatus не может быть null!")
            @RequestBody @Parameter(name = "newStatus", description = "Новый статус задачи", example = "EXPIRED") TaskStatus newStatus
    ) {
        Task updatedTask = taskService.setStatus(id, newStatus);
        return ResponseEntity.ok().body(TaskMapper.taskToTaskResponse(updatedTask));
    }


    /*TODO: TEST*/

    @Operation(summary = "Перемещение списка задач")
    @PreAuthorize("@taskSecurityExpression.canEditTask(#dstEventId)")
    @PutMapping("/event/{dstEventId}")
    public ResponseEntity<List<TaskResponse>> taskListMove(
            @Min(value = 1, message = "Параметр dstEventId не может быть меньше 1!")
            @PathVariable @Parameter(name = "dstEventId", description = "ID мероприятия, куда задача будет перемещена", example = "1") Integer dstEventId,
            @NotEmpty(message = "Список task id не может быть пустым!")
            @RequestBody List<Integer> taskIds
    ) {
        List<Task> updTasks = taskService.moveTasks(dstEventId, taskIds);
        return ResponseEntity.ok().body(TaskMapper.tasksToTaskResponseList(updTasks));
    }

    @Operation(summary = "Копирование списка задач")
    @PreAuthorize("@taskSecurityExpression.getCanCopyTasks(#dstEventId, #taskIds)")
    @PostMapping("/event/{dstEventId}")
    public ResponseEntity<List<TaskResponse>> taskListCopy(
            @Min(value = 1, message = "Параметр dstEventId не может быть меньше 1!")
            @PathVariable @Parameter(name = "dstEventId", description = "ID мероприятия, куда задача будет скопирована", example = "1") Integer dstEventId,
            @NotEmpty(message = "Список task id не может быть пустым!")
            @RequestBody List<Integer> taskIds
    ) {
        List<Task> newTasks = taskService.copyTasks(dstEventId, taskIds);
        // src == dst - ?
        return ResponseEntity.ok().body(TaskMapper.tasksToTaskResponseList(newTasks));
    }

    @Operation(summary = "Получение списка задач мероприятия")
    @PreAuthorize("@taskSecurityExpression.canGetEventTasks(#eventId)")
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<TaskResponse>> taskListShowInEvent(
            @Min(value = 1, message = "Параметр eventId не может быть меньше 1!")
            @PathVariable
            @Parameter(name = "eventId", description = "ID мероприятия", example = "1")
            Integer eventId,
            @Min(value = 1, message = "Параметр assigneeId не может быть меньше 1!")
            @RequestParam(required = false)
            @Parameter(name = "assigneeId", description = "ID Исполнителя задачи", example = "123")
            Integer assigneeId,
            @Min(value = 1, message = "Параметр assignerId не может быть меньше 1!")
            @RequestParam(required = false)
            @Parameter(name = "assignerId", description = "ID Создателя задачи", example = "13")
            Integer assignerId,
            @Valid @RequestParam(required = false)
            @Parameter(name = "taskStatus", description = "Статус задачи")
            TaskStatus taskStatus,
            @RequestParam(required = false)
            @Valid @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(name = "deadlineLowerLimit", description = "Нижняя граница для фильтрации задач по дедлайну")
            LocalDateTime deadlineLowerLimit,
            @RequestParam(required = false)
            @Valid @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(name = "deadlineUpperLimit", description = "Верхняя граница для фильтрации задач по дедлайну")
            LocalDateTime deadlineUpperLimit,
            @RequestParam(required = false, defaultValue = "false")
            @Parameter(name = "subEventTasksGet", description = "Включить получение задач активностей мероприятия")
            Boolean subEventTasksGet,
            @RequestParam(required = false, defaultValue = "false")
            @Parameter(name = "personalTasksGet", description = "Получить свои задачи в мероприятии. Более приоритетный параметр, чем assigneeId")
            Boolean personalTasksGet,
            @Min(value = 0, message = "Параметр page не может быть меньше 0!")
            @RequestParam(required = false, defaultValue = "0")
            @Parameter(name = "page", description = "Номер страницы")
            Integer page,
            @Min(value = 1, message = "Параметр pageSize не может быть меньше 1!")
            @RequestParam(required = false, defaultValue = "50")
            @Parameter(name = "pageSize", description = "Размер страницы")
            Integer pageSize
    ) {
        Integer userId = assigneeId;
        if (personalTasksGet) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            userId = userService.findByLogin(currentPrincipalName).getId();
        }

        Pageable pageRequest = PageRequest.of(page, pageSize, Sort.by("deadline"));
        Page<Task> eventTasks =
                taskService.getEventTasksWithFilter(eventId,
                        userId,
                        assignerId,
                        taskStatus,
                        deadlineLowerLimit,
                        deadlineUpperLimit,
                        subEventTasksGet,
                        pageRequest);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("totalElements", String.valueOf(eventTasks.getTotalElements()));

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(TaskMapper.tasksToTaskResponseList(eventTasks.toList()));
    }

    @Operation(summary = "Получение списка задач где пользователь является исполнителем")
    @GetMapping("/where-assignee")
    public ResponseEntity<List<TaskResponse>> taskListShowWhereAssignee(
            @Min(value = 1, message = "Параметр eventId не может быть меньше 1!")
            @RequestParam(required = false)
            @Parameter(name = "eventId", description = "ID мероприятия", example = "1")
            Integer eventId,
            @Min(value = 1, message = "Параметр assignerId не может быть меньше 1!")
            @RequestParam(required = false)
            @Parameter(name = "assignerId", description = "ID Создателя задачи", example = "13")
            Integer assignerId,
            @Valid @RequestParam(required = false)
            @Parameter(name = "taskStatus", description = "Статус задачи")
            TaskStatus taskStatus,
            @RequestParam(required = false)
            @Valid @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(name = "deadlineLowerLimit", description = "Нижняя граница для фильтрации задач по дедлайну")
            LocalDateTime deadlineLowerLimit,
            @RequestParam(required = false)
            @Valid @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(name = "deadlineUpperLimit", description = "Верхняя граница для фильтрации задач по дедлайну")
            LocalDateTime deadlineUpperLimit,
            @Min(value = 0, message = "Параметр page не может быть меньше 0!")
            @RequestParam(required = false, defaultValue = "0")
            @Parameter(name = "page", description = "Номер страницы")
            Integer page,
            @Min(value = 1, message = "Параметр pageSize не может быть меньше 1!")
            @RequestParam(required = false, defaultValue = "50")
            @Parameter(name = "pageSize", description = "Размер страницы")
            Integer pageSize
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Integer userId = userService.findByLogin(currentPrincipalName).getId();

        Pageable pageRequest = PageRequest.of(page, pageSize, Sort.by("deadline"));
        Page<Task> userTasks = taskService.getUserTasksWithFilter(eventId,
                userId,
                assignerId,
                taskStatus,
                deadlineLowerLimit,
                deadlineUpperLimit,
                pageRequest);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("totalElements", String.valueOf(userTasks.getTotalElements()));

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(TaskMapper.tasksToTaskResponseList(userTasks.toList()));
    }


}
