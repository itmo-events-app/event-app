package org.itmo.eventapp.main.util;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itmo.eventapp.main.mail.MailSenderService;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.repository.TaskRepository;
import org.itmo.eventapp.main.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskNotificationUtils {

    private final MailSenderService mailSenderService;

    private final NotificationService notificationService;

    private final TaskRepository taskRepository;

    @Value(value = "${notifications.taskUrl}")
    private String TASK_FULL_URL;

    @Value(value = "${notifications.cron.sending-period-in-minutes}")
    private Integer SENDING_PERIOD_IN_MINUTES;

    @Async
    public void createIncomingTaskNotification(Task task){
        Map<String, String> taskFields = taskMapper(task);
        String notificationTitle = "Новая задача!";
        String notificationDescription = "Вам назначена новая задача - " + taskFields.get("taskTitle")
                + " в мероприятии " + taskFields.get("taskEventTitle");

        notificationService.createNotification(notificationTitle,
                notificationDescription,
                Integer.parseInt(taskFields.get("taskAssigneeId")));

        try {
            mailSenderService.sendIncomingTaskMessage(
                    taskFields.get("taskAssigneeEmail"),
                    taskFields.get("taskAssigneeName"),
                    taskFields.get("taskEventTitle"),
                    taskFields.get("taskTitle"),
                    taskFields.get("taskUrl")
            );
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void createOverdueTaskNotification(Task task){
        Map<String, String> taskFields = taskMapper(task);
        String notificationTitle = "Просроченная задача!";
        String notificationDescription = "Прошёл срок исполнения задачи - " + taskFields.get("taskTitle")
                + " в мероприятии " + taskFields.get("taskEventTitle");

        notificationService.createNotification(notificationTitle,
                notificationDescription,
                Integer.parseInt(taskFields.get("taskAssigneeId")));
        notificationService.createNotification(notificationTitle,
                notificationDescription,
                Integer.parseInt(taskFields.get("taskAssignerId")));

        try {
            mailSenderService.sendIncomingTaskMessage(
                    taskFields.get("taskAssigneeEmail"),
                    taskFields.get("taskAssigneeName"),
                    taskFields.get("taskEventTitle"),
                    taskFields.get("taskTitle"),
                    taskFields.get("taskUrl")
            );
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            mailSenderService.sendIncomingTaskMessage(
                    taskFields.get("taskAssignerEmail"),
                    taskFields.get("taskAssignerName"),
                    taskFields.get("taskEventTitle"),
                    taskFields.get("taskTitle"),
                    taskFields.get("taskUrl")
            );
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void createReminderTaskNotification(Task task){
        Map<String, String> taskFields = taskMapper(task);
        String notificationTitle = "Не забудьте выполнить задачу!";
        String notificationDescription = "Не забудьте выполнить задачу - " + taskFields.get("taskTitle")
                + " в мероприятии " + taskFields.get("taskEventTitle");

        notificationService.createNotification(notificationTitle,
                notificationDescription,
                Integer.parseInt(taskFields.get("taskAssigneeId")));

        try {
            mailSenderService.sendIncomingTaskMessage(
                    taskFields.get("taskAssigneeEmail"),
                    taskFields.get("taskAssigneeName"),
                    taskFields.get("taskEventTitle"),
                    taskFields.get("taskTitle"),
                    taskFields.get("taskUrl")
            );
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = "${notifications.cron.create-notification-job}")
    public void sendNotificationsOnDeadline(){
        System.out.println("DEADLINE JOB STARTED:" + LocalDateTime.now().toString());
        LocalDateTime endTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime startTime = endTime.minusMinutes(SENDING_PERIOD_IN_MINUTES);

        List<Task> overdueTasks = getTasksWithDeadlineBetween(startTime, endTime);

        for (Task task: overdueTasks) {
            createOverdueTaskNotification(task);
        }
    }

    @Scheduled(cron = "${notifications.cron.create-notification-job}")
    public void sendNotificationsOnNotificationDeadline(){
        LocalDateTime endTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime startTime = endTime.minusMinutes(SENDING_PERIOD_IN_MINUTES);

        List<Task> overdueTasks = getTasksWithNotificationDeadlineBetween(startTime, endTime);

        for (Task task: overdueTasks) {
            createReminderTaskNotification(task);
        }
    }

    private Map<String, String> taskMapper(Task task){
        return Stream.of(new String[][] {
                { "taskId", task.getId().toString() },
                { "taskTitle", task.getTitle() },
                { "taskAssignerId", task.getAssigner().getId().toString() },
                { "taskAssignerName", task.getAssigner().getName() },
                { "taskAssignerEmail", task.getAssigner().getUserLoginInfo().getEmail() },
                { "taskAssigneeId", task.getAssigner().getId().toString() },
                { "taskAssigneeName", task.getAssignee().getName() },
                { "taskAssigneeEmail", task.getAssignee().getUserLoginInfo().getEmail() },
                { "taskEventTitle", task.getEvent().getTitle() },
                { "taskUrl", TASK_FULL_URL + task.getId().toString() },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
    }

    private List<Task> getTasksWithDeadlineBetween(LocalDateTime startTime, LocalDateTime endTime){
        return taskRepository.findAllByDeadlineBetween(startTime, endTime);
    }

    private List<Task> getTasksWithNotificationDeadlineBetween(LocalDateTime startTime, LocalDateTime endTime){
        return taskRepository.findAllByNotificationDeadlineBetween(startTime, endTime);
    }

}
