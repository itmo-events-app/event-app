package org.itmo.eventapp.main.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itmo.eventapp.main.mail.MailSenderService;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.service.NotificationService;
import org.itmo.eventapp.main.service.TaskService;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskNotificationUtils {

    private final MailSenderService mailSenderService;

    private final NotificationService notificationService;

    private final TaskService taskService;

    public static void createIncomingTaskNotification(Task task){

    }

    public void createOverdueTaskNotification(){

    }

    public void createReminderTaskNotification(){

    }

}
