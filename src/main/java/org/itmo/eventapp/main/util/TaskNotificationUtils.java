package org.itmo.eventapp.main.util;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.itmo.eventapp.main.mail.MailSenderService;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.service.NotificationService;
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
        String notificationDescription = String.format("Вам назначена новая задача - %s в мероприятии %s.",
                task.getTitle(), task.getEvent().getTitle());

        notificationService.createNotification(notificationTitle,
                notificationDescription,
                task.getAssignee().getId(),
                taskFullUrl + task.getId().toString());

        mailSenderService.sendIncomingTaskMessage(
                task.getAssignee().getUserLoginInfo().getLogin(),
                task.getAssignee().getName(),
                task.getEvent().getTitle(),
                task.getTitle(),
                taskFullUrl + task.getId().toString());
    }

    @Async
    @SneakyThrows
    public void createOverdueTaskNotification(Task task){
        String notificationTitle = "Просроченная задача!";
        String notificationDescription = String.format("Прошёл срок исполнения задачи - %s в мероприятии %s.",
                task.getTitle(), task.getEvent().getTitle());

        notificationService.createNotification(notificationTitle,
                notificationDescription,
                task.getAssignee().getId(),
                taskFullUrl + task.getId().toString());
        notificationService.createNotification(notificationTitle,
                notificationDescription,
                task.getAssigner().getId(),
                taskFullUrl + task.getId().toString());

        mailSenderService.sendOverdueTaskMessage(
                task.getAssignee().getUserLoginInfo().getLogin(),
                task.getAssignee().getName(),
                task.getEvent().getTitle(),
                task.getTitle(),
                taskFullUrl + task.getId().toString());

        mailSenderService.sendOverdueTaskMessage(
                task.getAssigner().getUserLoginInfo().getLogin(),
                task.getAssigner().getName(),
                task.getEvent().getTitle(),
                task.getTitle(),
                taskFullUrl + task.getId().toString());
    }

    @Async
    @SneakyThrows
    public void createReminderTaskNotification(Task task){
        String notificationTitle = "Не забудьте выполнить задачу!";
        String notificationDescription = String.format("Не забудьте выполнить задачу - %s в мероприятии %s.",
                task.getTitle(), task.getEvent().getTitle());

        notificationService.createNotification(notificationTitle,
                notificationDescription,
                task.getAssignee().getId(),
                taskFullUrl + task.getId().toString());

        mailSenderService.sendReminderTaskMessage(
                task.getAssignee().getUserLoginInfo().getLogin(),
                task.getAssignee().getName(),
                task.getEvent().getTitle(),
                task.getTitle(),
                taskFullUrl + task.getId().toString());
    }
}
