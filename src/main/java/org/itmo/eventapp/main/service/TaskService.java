package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.minio.MinioService;
import org.itmo.eventapp.main.model.dto.request.TaskRequest;
import org.itmo.eventapp.main.model.dto.response.FileDataResponse;
import org.itmo.eventapp.main.model.entity.*;
import org.itmo.eventapp.main.model.entity.enums.TaskStatus;
import org.itmo.eventapp.main.model.mapper.TaskMapper;
import org.itmo.eventapp.main.repository.TaskRepository;
import org.itmo.eventapp.main.util.TaskNotificationUtils;
import org.springframework.context.annotation.Lazy;
import org.itmo.eventapp.main.service.specification.TaskSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

@RequiredArgsConstructor(onConstructor_ = {@Lazy})
@Service
public class TaskService {

    private static final String BUCKET_NAME = "task-objects";

    @Lazy
    private final EventService eventService;
    private final UserService userService;
    private final PlaceService placeService;
    private final TaskRepository taskRepository;
    private final TaskNotificationUtils taskNotificationUtils;
    private final TaskReminderTriggerService taskReminderTriggerService;
    private final TaskDeadlineTriggerService taskDeadlineTriggerService;
    private final MinioService minioService;

    public Task findById(int id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.TASK_NOT_FOUND_MESSAGE));
    }

    public List<Task> findAllById(List<Integer> ids) {
        return taskRepository.findAllById(ids);
    }

    @Transactional
    public Task save(TaskRequest taskRequest) {

        Event event = eventService.findById(taskRequest.eventId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User assigner = userService.findByLogin(email);


        User assignee = null;
        if (taskRequest.assigneeId() != null) {
            assignee = userService.findById(taskRequest.assigneeId());
        }
        Place place = null;
        if (taskRequest.placeId() != null) {
            place = placeService.findById(taskRequest.placeId());
        }

        Task newTask = TaskMapper.taskRequestToTask(taskRequest, event, assignee, assigner, place);

        TaskStatus status = TaskStatus.NEW;
        if (LocalDateTime.now().isAfter(newTask.getDeadline())) {
            status = TaskStatus.EXPIRED;
        }

        newTask.setStatus(status);
        newTask.setCreationTime(LocalDateTime.now());

        newTask = taskRepository.save(newTask);

        if (assignee != null) {
            taskNotificationUtils.createIncomingTaskNotification(newTask);
            taskDeadlineTriggerService.createNewDeadlineTrigger(newTask);
            taskReminderTriggerService.createNewReminderTrigger(newTask);
        }

        return newTask;
    }

    @Transactional
    public Task edit(Integer id, TaskRequest taskRequest) {

        Task task = taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.TASK_NOT_FOUND_MESSAGE));

        Event event = eventService.findById(taskRequest.eventId());
        User assigner = task.getAssigner();

        User prevAssignee = task.getAssignee();

        User assignee = null;
        if (taskRequest.assigneeId() != null) {
            assignee = userService.findById(taskRequest.assigneeId());

        }
        Place place = null;
        if (taskRequest.placeId() != null) {
            place = placeService.findById(taskRequest.placeId());
        }

        Task newTaskData = TaskMapper.taskRequestToTask(taskRequest, event, assignee, assigner, place);
        newTaskData.setId(task.getId());
        newTaskData.setCreationTime(task.getCreationTime());
        if (LocalDateTime.now().isAfter(newTaskData.getDeadline())) {
            newTaskData.setStatus(TaskStatus.EXPIRED);
        }

        newTaskData = taskRepository.save(newTaskData);

        if (assignee != null && (prevAssignee == null || !Objects.equals(prevAssignee.getId(), assignee.getId()))) {

            taskNotificationUtils.createIncomingTaskNotification(newTaskData);
            taskDeadlineTriggerService.createNewDeadlineTrigger(newTaskData);
            taskReminderTriggerService.createNewReminderTrigger(newTaskData);

        }

        return newTaskData;
    }

    public void delete(Integer id) {
        minioService.deleteImageByPrefix(BUCKET_NAME, id.toString());
        taskRepository.deleteById(id);
    }


    public List<String> addFiles(Integer id, List<MultipartFile> files) {

        Task task = taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.TASK_NOT_FOUND_MESSAGE));

        List<String> filenames = new ArrayList<>();

        if (!Objects.isNull(files)) {

            for (MultipartFile file : files) {

                String modifiedFileName = task.getId().toString()
                    + "_"
                    + FilenameUtils.getBaseName(file.getOriginalFilename())
                    + "_"
                    + System.currentTimeMillis()
                    + "."
                    + FilenameUtils.getExtension(file.getOriginalFilename());
                minioService.uploadWithModifiedFileName(file, BUCKET_NAME, modifiedFileName);
                filenames.add(modifiedFileName);
            }

        }

        return filenames;

    }


    public void deleteFiles(Integer id, List<String> fileNamesInMinio) {

        Task task = taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.TASK_NOT_FOUND_MESSAGE));

        boolean allBelong = fileNamesInMinio.stream().allMatch(filename -> filename.startsWith(task.getId().toString()));
        if (!allBelong) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionConst.INVALID_TASK_FILE_NAMES_MESSAGE);
        }

        for (String filename : fileNamesInMinio) {
            minioService.delete(BUCKET_NAME, filename);
        }

    }

    public List<String> getFileNames(Integer taskId) {
        return minioService.getFileNamesByPrefix(BUCKET_NAME, taskId.toString());
    }


    public List<FileDataResponse> getFileData(Integer taskId) {
        return minioService.getFileDataByPrefix(BUCKET_NAME, taskId.toString());
    }


    @Transactional
    public Task setAssignee(Integer taskId, Integer assigneeId) {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.TASK_NOT_FOUND_MESSAGE));

        User prevAssignee = task.getAssignee();

        User assignee = null;
        if (assigneeId != -1) { // -1 stands for delete option
            assignee = userService.findById(assigneeId); // find by id from request
        }
        task.setAssignee(assignee);

        task = taskRepository.save(task);

        if (assignee != null && (prevAssignee == null || !Objects.equals(prevAssignee.getId(), assignee.getId()))) {

            taskNotificationUtils.createIncomingTaskNotification(task);
            taskDeadlineTriggerService.createNewDeadlineTrigger(task);
            taskReminderTriggerService.createNewReminderTrigger(task);

        }

        return task;

    }


    public Task setStatus(Integer taskId, TaskStatus taskStatus) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.TASK_NOT_FOUND_MESSAGE));
        task.setStatus(taskStatus);
        task = taskRepository.save(task);
        return task;
    }


    public List<Task> moveTasks(Integer dstEventId, List<Integer> taskIds) {

        Event event = eventService.findById(dstEventId);
        List<Task> tasks = taskRepository.findAllById(taskIds);

        for (Task task : tasks) {
            if (!eventService.checkOneEvent(event, task.getEvent())) {
                throw new IllegalArgumentException("Нельзя переносить задачи между разными мероприятиями! Попроуйте копирование.");
            }
        }

        for (Task task : tasks) {
            task.setEvent(event);
        }

        return taskRepository.saveAll(tasks);
    }


    public List<Task> copyTasks(Integer dstEventId, List<Integer> taskIds) {

        Event event = eventService.findById(dstEventId);
        List<Task> tasks = taskRepository.findAllById(taskIds);

        List<Task> newTasks = new ArrayList<>();
        List<String> prefixes = new ArrayList<>();

        for (Task task : tasks) {

            Task newTask = new Task();
            newTask.setEvent(event);
            newTask.setTitle(task.getTitle());
            newTask.setDescription(task.getDescription());
            newTask.setPlace(task.getPlace());
            newTask.setAssigner(task.getAssigner());
            newTask.setAssignee(null);
            newTask.setDeadline(task.getDeadline());
            newTask.setReminder(task.getReminder());

            newTask.setCreationTime(LocalDateTime.now());
            TaskStatus status = TaskStatus.NEW;
            if (LocalDateTime.now().isAfter(newTask.getDeadline())) {
                status = TaskStatus.EXPIRED;
            }
            newTask.setStatus(status);

            newTasks.add(newTask);
            prefixes.add(task.getId().toString());
        }

        newTasks = taskRepository.saveAll(newTasks);

        for (int i = 0; i < newTasks.size(); i++) {

            minioService.copyImagesWithPrefix(BUCKET_NAME, BUCKET_NAME, prefixes.get(i), newTasks.get(i).getId().toString());

        }

        return newTasks;
    }

    public Page<Task> getEventTasksWithFilter(Integer eventId,
                                              Integer assigneeId,
                                              Integer assignerId,
                                              TaskStatus taskStatus,
                                              LocalDateTime deadlineLowerLimit,
                                              LocalDateTime deadlineUpperLimit,
                                              Boolean subEventTasksGet,
                                              Pageable pageRequest) {

        Event event = eventService.findById(eventId);

        if (event.getParent() == null && subEventTasksGet) {
            List<Integer> ids = eventService.getAllSubEventIds(event.getId());
            List<Integer> idsWithParent = new ArrayList<>();
            idsWithParent.add(event.getId());
            idsWithParent.addAll(ids);

            Specification<Task> taskSpecification =
                TaskSpecification.filterByEventIdsListAndExtraParams(idsWithParent,
                    assigneeId,
                    assignerId,
                    taskStatus,
                    deadlineLowerLimit,
                    deadlineUpperLimit);
            return taskRepository.findAll(taskSpecification, pageRequest);

        } else {
            Specification<Task> taskSpecification =
                TaskSpecification.filterByEventIdAndExtraParams(eventId,
                    assigneeId,
                    assignerId,
                    taskStatus,
                    deadlineLowerLimit,
                    deadlineUpperLimit);
            return taskRepository.findAll(taskSpecification, pageRequest);
        }


    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    List<Task> findAllByEventId(Integer eventId) {
        return taskRepository.findAllByEventId(eventId);
    }


    public Page<Task> getUserTasksWithFilter(Integer eventId,
                                             Integer userId,
                                             Integer assignerId,
                                             TaskStatus taskStatus,
                                             LocalDateTime deadlineLowerLimit,
                                             LocalDateTime deadlineUpperLimit,
                                             Pageable pageRequest) {


        Specification<Task> taskSpecification =
            TaskSpecification.filterByEventIdAndExtraParams(eventId,
                userId,
                assignerId,
                taskStatus,
                deadlineLowerLimit,
                deadlineUpperLimit);
        return taskRepository.findAll(taskSpecification, pageRequest);
    }


}
