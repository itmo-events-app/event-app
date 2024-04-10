package org.itmo.eventapp.main.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.model.entity.TaskDeadlineTrigger;
import org.itmo.eventapp.main.model.entity.TaskReminderTrigger;
import org.itmo.eventapp.main.service.NotificationService;
import org.itmo.eventapp.main.service.TaskDeadlineTriggerService;
import org.itmo.eventapp.main.service.TaskReminderTriggerService;
import org.itmo.eventapp.main.service.TaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledJobsUtils {
    private final TaskDeadlineTriggerService taskDeadlineTriggerService;
    private final TaskReminderTriggerService taskReminderTriggerService;
    private final TaskNotificationUtils taskNotificationUtils;
    private final NotificationService notificationService;

    @Value(value = "${notifications.cron.delete-period-in-days}")
    private Integer deletePeriodInDays;

    @Scheduled(cron = "${notifications.cron.create-notification-job}")
    public void handleTaskDeadline(){
        LocalDateTime deadline = LocalDateTime.now();
        List<Task> overdueTasks = taskDeadlineTriggerService.updateAndRetrieveTaskOnDeadline(deadline);
        overdueTasks.forEach(taskNotificationUtils::createOverdueTaskNotification);
    }

    @Scheduled(cron = "${notifications.cron.create-notification-job}")
    public void handleTaskReminder(){
        LocalDateTime deadline = LocalDateTime.now();
        List<Task> overdueTasks = taskReminderTriggerService.retrieveTasksOnReminder(deadline);
        overdueTasks.forEach(taskNotificationUtils::createReminderTaskNotification);
    }

    @Scheduled(cron = "${notifications.cron.delete-notification-job}")
    public void deleteOutdatedNotifications(){
        LocalDateTime beforeTime = LocalDateTime.now().minusDays(deletePeriodInDays);
        notificationService.deleteNotificationsBeforeSentTime(beforeTime);
    }
}
