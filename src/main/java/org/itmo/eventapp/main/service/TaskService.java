package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.dto.request.TaskRequest;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.Place;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.model.entity.enums.TaskStatus;
import org.itmo.eventapp.main.model.mapper.TaskMapper;
import org.itmo.eventapp.main.repository.TaskRepository;
import org.itmo.eventapp.main.util.TaskNotificationUtils;
import org.springframework.context.annotation.Lazy;
import org.itmo.eventapp.main.service.specification.TaskSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = {@Lazy})
@Service
public class TaskService {
    @Lazy
    private final EventService eventService;
    private final UserService userService;
    private final PlaceService placeService;
    private final TaskRepository taskRepository;
    private final TaskNotificationUtils taskNotificationUtils;

    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    public Task save(TaskRequest taskRequest) {

        Event event = eventService.findById(taskRequest.eventId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User assigner = userService.findByEmail(email);


        User assignee = null;
        if (taskRequest.assignee() != null) {
            assignee = userService.findById(taskRequest.assignee().id());
        }
        Place place = null;
        if (taskRequest.place() != null) {
            place = placeService.findById(taskRequest.place().id());
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
        }

        return newTask;
    }

    public Task edit(Integer id, TaskRequest taskRequest) {

        Task task = taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.TASK_NOT_FOUND_MESSAGE));

        Event event = eventService.findById(taskRequest.eventId());
        User assigner = task.getAssigner();

        User prevAssignee = task.getAssignee();

        User assignee = null;
        if (taskRequest.assignee() != null) {
            assignee = userService.findById(taskRequest.assignee().id());

        }
        Place place = null;
        if (taskRequest.place() != null) {
            place = placeService.findById(taskRequest.place().id());
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

        }

        return newTaskData;
    }

    public void delete(Integer id) {

        taskRepository.deleteById(id);

    }


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
        for (Task task : tasks) {

            Task newTask = new Task();
            newTask.setEvent(event);
            newTask.setTitle(task.getTitle());
            newTask.setDescription(task.getDescription());
            newTask.setPlace(task.getPlace());
            newTask.setAssigner(task.getAssigner());
            newTask.setAssignee(null);
            newTask.setDeadline(task.getDeadline());
            newTask.setNotificationDeadline(task.getNotificationDeadline());

            newTask.setCreationTime(LocalDateTime.now());
            TaskStatus status = TaskStatus.NEW;
            if (LocalDateTime.now().isAfter(newTask.getDeadline())) {
                status = TaskStatus.EXPIRED;
            }
            newTask.setStatus(status);

            newTasks.add(newTask);
        }

        newTasks = taskRepository.saveAll(newTasks);

        return newTasks;
    }

    public List<Task> getEventTasksWithFilter(Integer eventId,
                                              Integer assigneeId,
                                              Integer assignerId,
                                              TaskStatus taskStatus,
                                              LocalDateTime deadlineLowerLimit,
                                              LocalDateTime deadlineUpperLimit,
                                              Boolean subEventTasksGet) {

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
            return taskRepository.findAll(taskSpecification);

        } else {
            Specification<Task> taskSpecification =
                    TaskSpecification.filterByEventIdAndExtraParams(eventId,
                            assigneeId,
                            assignerId,
                            taskStatus,
                            deadlineLowerLimit,
                            deadlineUpperLimit);
            return taskRepository.findAll(taskSpecification);
        }


    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    List<Task> findAllByEventId(Integer eventId) {
        return taskRepository.findAllByEventId(eventId);
    }


    public List<Task> getUserTasksWithFilter(Integer eventId,
                                             Integer userId,
                                             Integer assignerId,
                                             TaskStatus taskStatus,
                                             LocalDateTime deadlineLowerLimit,
                                             LocalDateTime deadlineUpperLimit) {


        Specification<Task> taskSpecification =
                TaskSpecification.filterByEventIdAndExtraParams(eventId,
                        userId,
                        assignerId,
                        taskStatus,
                        deadlineLowerLimit,
                        deadlineUpperLimit);
        return taskRepository.findAll(taskSpecification);
    }

    public List<Task> getTasksWithDeadlineBetween(LocalDateTime startTime, LocalDateTime endTime){
        return taskRepository.findAllByDeadlineBetween(startTime, endTime);
    }

    public List<Task> getTasksWithNotificationDeadlineBetween(LocalDateTime startTime, LocalDateTime endTime){
        return taskRepository.findAllByNotificationDeadlineBetween(startTime, endTime);
    }

}
