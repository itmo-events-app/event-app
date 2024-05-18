package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.minio.MinioService;
import org.itmo.eventapp.main.model.dto.request.TaskRequest;
import org.itmo.eventapp.main.model.dto.response.FileDataResponse;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.Place;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.model.entity.enums.TaskStatus;
import org.itmo.eventapp.main.model.mapper.TaskMapper;
import org.itmo.eventapp.main.repository.TaskRepository;
import org.itmo.eventapp.main.service.specification.TaskSpecification;
import org.itmo.eventapp.main.util.TaskNotificationUtils;
import org.springframework.context.annotation.Lazy;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

        LocalDateTime currentTime = LocalDateTime.now();

        if (assignee != null) {
            taskNotificationUtils.createIncomingTaskNotification(newTask);
            if (taskRequest.deadline().isAfter(currentTime)) taskDeadlineTriggerService.createNewDeadlineTrigger(newTask);
            if (taskRequest.reminder().isAfter(currentTime)) taskReminderTriggerService.createNewReminderTrigger(newTask);
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

        LocalDateTime currentTime = LocalDateTime.now();

        if (assignee != null && (prevAssignee == null || !Objects.equals(prevAssignee.getId(), assignee.getId()))) {

            taskNotificationUtils.createIncomingTaskNotification(newTaskData);
            if (taskRequest.deadline().isAfter(currentTime)) taskDeadlineTriggerService.createNewDeadlineTrigger(newTaskData);
            if (taskRequest.reminder().isAfter(currentTime)) taskReminderTriggerService.createNewReminderTrigger(newTaskData);

        }

        return newTaskData;
    }

    public void delete(Integer id) {
        minioService.deleteImageByPrefix(BUCKET_NAME, id.toString() + "_");
        taskRepository.deleteById(id);
    }

    public void deleteAllByActivityId(Integer eventId) {

        Event activity = eventService.getEventById(eventId);
        if (activity.getParent() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionConst.EVENT_DELETION_FORBIDDEN_MESSAGE);
        }

        List<Task> tasksToDelete = taskRepository.findAllByEventId(eventId);
        for (Task task : tasksToDelete) {
            minioService.deleteImageByPrefix(BUCKET_NAME, task.getId().toString() + "_");
        }
        taskRepository.deleteAll(tasksToDelete);
    }


    public List<FileDataResponse> addFiles(Integer id, List<MultipartFile> files) {

        Task task = taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.TASK_NOT_FOUND_MESSAGE));

        if (!Objects.isNull(files)) {

            for (MultipartFile file : files) {

                String modifiedFileName = task.getId().toString()
                        + "_"
                        + FilenameUtils.getBaseName(file.getOriginalFilename())
                        + "__"
                        + LocalDateTime.now()
                        + "."
                        + FilenameUtils.getExtension(file.getOriginalFilename());
                minioService.uploadWithModifiedFileName(file, BUCKET_NAME, modifiedFileName);
            }

        }

        return getFileData(id);

    }


    public void deleteFiles(Integer id, List<String> fileNamesInMinio) {

        Task task = taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.TASK_NOT_FOUND_MESSAGE));

        boolean allBelong = fileNamesInMinio.stream().allMatch(filename -> filename.startsWith(task.getId().toString() + "_"));
        if (!allBelong) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionConst.INVALID_TASK_FILE_NAMES_MESSAGE);
        }

        for (String filename : fileNamesInMinio) {
            minioService.delete(BUCKET_NAME, filename);
        }

    }

    public List<String> getFileNames(Integer taskId) {
        return minioService.getFileNamesByPrefix(BUCKET_NAME, taskId.toString() + "_");
    }


    public List<FileDataResponse> getFileData(Integer taskId) {
        return minioService.getFileDataByPrefix(BUCKET_NAME, taskId.toString() + "_");
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

        LocalDateTime currentTime = LocalDateTime.now();

        if (assignee != null && (prevAssignee == null || !Objects.equals(prevAssignee.getId(), assignee.getId()))) {

            taskNotificationUtils.createIncomingTaskNotification(task);
            if (task.getDeadline().isAfter(currentTime)) taskDeadlineTriggerService.createNewDeadlineTrigger(task);
            if (task.getReminder().isAfter(currentTime)) taskReminderTriggerService.createNewReminderTrigger(task);

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

    public List<Task> copyTasksWithEventAlreadyFetched(Event event, List<Task> tasks) {

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

            LocalDateTime now = LocalDateTime.now();
            Duration deadlineInterval = Duration.between(task.getDeadline(), task.getCreationTime());
            Duration reminderInterval = Duration.between(task.getReminder(), task.getCreationTime());

            newTask.setCreationTime(now);
            newTask.setDeadline(now.plusNanos(deadlineInterval.toNanos()));
            newTask.setReminder(now.plusNanos(reminderInterval.toNanos()));

            newTask.setStatus(TaskStatus.NEW);

            newTasks.add(newTask);
            prefixes.add(task.getId().toString() + "_");
        }

        newTasks = taskRepository.saveAll(newTasks);

        for (int i = 0; i < newTasks.size(); i++) {

            minioService.copyImagesWithPrefix(BUCKET_NAME, BUCKET_NAME, prefixes.get(i), newTasks.get(i).getId().toString() + "_");

        }

        return newTasks;
    }

    public List<Task> copyTasks(Integer dstEventId, List<Integer> taskIds) {

        Event event = eventService.findById(dstEventId);
        List<Task> tasks = taskRepository.findAllById(taskIds);

        return copyTasksWithEventAlreadyFetched(event, tasks);

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


    public Event copyEventWithTasks(Integer srcEventId, boolean deep) {

        Event existingEvent = eventService.findById(srcEventId);
        Event savedEvent = eventService.copyEventByOne(existingEvent, existingEvent.getParent());
        List<Task> tasks = findAllByEventId(srcEventId);
        copyTasksWithEventAlreadyFetched(savedEvent, tasks);

        if (deep) {
            List<Event> childEvents = eventService.findAllByParentId(existingEvent.getId());
            childEvents.forEach(childEvent -> {
                Event childOfSavedEvent = eventService.copyEventByOne(childEvent, savedEvent);
                List<Task> childTasks = findAllByEventId(childEvent.getId());
                copyTasksWithEventAlreadyFetched(childOfSavedEvent, childTasks);
            });
        }
        return savedEvent;


    }

    public Event createEventBasedOnExistingAndCopyTasks(Integer eventId, String title, Integer userId, boolean deep){
        Event copiedEventWithTasks = copyEventWithTasks(eventId, deep);
        return eventService.createEventBasedOnExistingWithNewTitleAndAdmin(copiedEventWithTasks.getId(), title, userId);
    }
}
