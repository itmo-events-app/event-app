package org.itmo.eventapp.main.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.service.NotificationService;
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
public class ScheduledJobsUtil {
    private final TaskService taskService;
    private final TaskNotificationUtils taskNotificationUtils;
    private final NotificationService notificationService;

    @Value(value = "${notifications.taskUrl}")
    private String taskFullUrl;

    @Value(value = "${notifications.cron.sending-period-in-minutes}")
    private Integer sendingPeriodInMinutes;

    @Value(value = "${notifications.cron.delete-period-in-days}")
    private Integer deletePeriodInDays;

    @Scheduled(cron = "${notifications.cron.create-notification-job}")
    public void sendNotificationsOnDeadline(){
        LocalDateTime endTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime startTime = endTime.minusMinutes(sendingPeriodInMinutes);

        List<Task> overdueTasks = taskService.getTasksWithDeadlineBetween(startTime, endTime);

        overdueTasks.forEach(taskNotificationUtils::createOverdueTaskNotification);
    }

    @Scheduled(cron = "${notifications.cron.create-notification-job}")
    public void sendNotificationsOnReminder(){
        LocalDateTime endTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime startTime = endTime.minusMinutes(sendingPeriodInMinutes);

        List<Task> overdueTasks = taskService.getTasksWithReminderBetween(startTime, endTime);

        overdueTasks.forEach(taskNotificationUtils::createReminderTaskNotification);
    }

    @Scheduled(cron = "${notifications.cron.delete-notification-job}")
    public void deleteOutdatedNotifications(){
        LocalDateTime beforeTime = LocalDateTime.now().minusDays(deletePeriodInDays);
        notificationService.deleteNotificationsBeforeSentTime(beforeTime);
    }
}
