package org.itmo.eventapp.main.util;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.itmo.eventapp.main.mail.MailSenderService;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.service.NotificationService;
import org.itmo.eventapp.main.service.TaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskNotificationUtils {

    private final MailSenderService mailSenderService;
    private final NotificationService notificationService;


    @Value(value = "${notifications.taskUrl}")
    private String taskFullUrl;

    @Async
    @SneakyThrows
    public void createIncomingTaskNotification(Task task){
        String notificationTitle = "Новая задача!";
        String notificationDescription = "Вам назначена новая задача - " + task.getTitle()
                + " в мероприятии " + task.getEvent().getTitle();

        notificationService.createNotification(notificationTitle,
                notificationDescription,
                task.getAssignee().getId());

        mailSenderService.sendIncomingTaskMessage(
                task.getAssignee().getUserLoginInfo().getEmail(),
                task.getAssignee().getName(),
                task.getEvent().getTitle(),
                task.getTitle(),
                taskFullUrl + task.getId().toString());
    }

    @Async
    @SneakyThrows
    public void createOverdueTaskNotification(Task task){
        String notificationTitle = "Просроченная задача!";
        String notificationDescription = "Прошёл срок исполнения задачи - " + task.getTitle()
                + " в мероприятии " + task.getEvent().getTitle();

        notificationService.createNotification(notificationTitle,
                notificationDescription,
                task.getAssignee().getId());
        notificationService.createNotification(notificationTitle,
                notificationDescription,
                task.getAssigner().getId());

        mailSenderService.sendOverdueTaskMessage(
                task.getAssignee().getUserLoginInfo().getEmail(),
                task.getAssignee().getName(),
                task.getEvent().getTitle(),
                task.getTitle(),
                taskFullUrl + task.getId().toString());

        mailSenderService.sendOverdueTaskMessage(
                task.getAssigner().getUserLoginInfo().getEmail(),
                task.getAssigner().getName(),
                task.getEvent().getTitle(),
                task.getTitle(),
                taskFullUrl + task.getId().toString());
    }

    @Async
    @SneakyThrows
    public void createReminderTaskNotification(Task task){
        String notificationTitle = "Не забудьте выполнить задачу!";
        String notificationDescription = "Не забудьте выполнить задачу - " + task.getTitle()
                + " в мероприятии " + task.getEvent().getTitle();

        notificationService.createNotification(notificationTitle,
                notificationDescription,
                task.getAssignee().getId());

        mailSenderService.sendReminderTaskMessage(
                task.getAssignee().getUserLoginInfo().getEmail(),
                task.getAssignee().getName(),
                task.getEvent().getTitle(),
                task.getTitle(),
                taskFullUrl + task.getId().toString());
    }
}