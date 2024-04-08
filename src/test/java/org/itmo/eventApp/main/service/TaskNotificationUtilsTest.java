package org.itmo.eventApp.main.service;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.itmo.eventApp.main.controller.AbstractTestContainers;
import org.itmo.eventapp.main.model.entity.*;
import org.itmo.eventapp.main.repository.NotificationRepository;
import org.itmo.eventapp.main.util.TaskNotificationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TaskNotificationUtilsTest extends AbstractTestContainers {
    @Autowired
    private TaskNotificationUtils taskNotificationUtils;

    @Autowired
    private NotificationRepository notificationRepository;

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("sender@test", "passwd"))
            .withPerMethodLifecycle(true);

    private void databaseFilling(){
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
    }

    private Task createTaskForNotification(){
        return Task.builder()
                .id(1)
                .title("TestTask")
                .event(Event.builder().title("EventTitle").build())
                .assignee(User.builder().id(1).name("HelperName")
                        .userLoginInfo(UserLoginInfo.builder().login("test_mail@itmo.ru").build()).build())
                .assigner(User.builder().id(2).name("OrganizerName")
                        .userLoginInfo(UserLoginInfo.builder().login("test_mail1@itmo.ru").build()).build())
                .build();
    }

    @Test
    void correctCreateIncomingTaskNotificationTest() throws Exception {
        databaseFilling();

        taskNotificationUtils.createIncomingTaskNotification(createTaskForNotification());

        String expectedTitle = "Новая задача!";
        String expectedDescription = "Вам назначена новая задача - TestTask в мероприятии EventTitle";

        Awaitility.await().atMost(5, TimeUnit.SECONDS).until(() -> greenMail.getReceivedMessages().length > 0);

        assertEquals(1, greenMail.getReceivedMessages().length);

        Notification notification = notificationRepository.findById(1).orElseThrow(() ->new Exception("NotificationNotFound"));

        assertEquals(expectedTitle, notification.getTitle());
        assertEquals(expectedDescription, notification.getDescription());
        assertNotNull(notification.getSentTime());
        assertFalse(notification.isSeen());
        assertEquals(1, notification.getUser().getId());
    }

    @Test
    void correctReminderIncomingTaskNotificationTest() throws Exception {
        databaseFilling();

        taskNotificationUtils.createReminderTaskNotification(createTaskForNotification());

        String expectedTitle = "Не забудьте выполнить задачу!";
        String expectedDescription = "Не забудьте выполнить задачу - TestTask в мероприятии EventTitle";

        Awaitility.await().atMost(5, TimeUnit.SECONDS).until(() -> greenMail.getReceivedMessages().length > 0);

        assertEquals(1, greenMail.getReceivedMessages().length);

        Notification notification = notificationRepository.findById(1).orElseThrow(() ->new Exception("NotificationNotFound"));

        assertEquals(expectedTitle, notification.getTitle());
        assertEquals(expectedDescription, notification.getDescription());
        assertNotNull(notification.getSentTime());
        assertFalse(notification.isSeen());
        assertEquals(1, notification.getUser().getId());
    }

    @Test
    void correctOverdueIncomingTaskNotificationTest() throws Exception {
        databaseFilling();

        taskNotificationUtils.createOverdueTaskNotification(createTaskForNotification());

        String expectedTitle = "Просроченная задача!";
        String expectedDescription = "Прошёл срок исполнения задачи - TestTask в мероприятии EventTitle";

        Awaitility.await().atMost(5, TimeUnit.SECONDS).until(() -> greenMail.getReceivedMessages().length > 1);

        assertEquals(2, greenMail.getReceivedMessages().length);

        Notification notificationAssignee = notificationRepository.findById(1).orElseThrow(() ->new Exception("NotificationNotFound"));

        assertEquals(expectedTitle, notificationAssignee.getTitle());
        assertEquals(expectedDescription, notificationAssignee.getDescription());
        assertNotNull(notificationAssignee.getSentTime());
        assertFalse(notificationAssignee.isSeen());
        assertEquals(1, notificationAssignee.getUser().getId());

        Notification notificationAssigner = notificationRepository.findById(2).orElseThrow(() ->new Exception("NotificationNotFound"));

        assertEquals(expectedTitle, notificationAssigner.getTitle());
        assertEquals(expectedDescription, notificationAssigner.getDescription());
        assertNotNull(notificationAssigner.getSentTime());
        assertFalse(notificationAssigner.isSeen());
        assertEquals(2, notificationAssigner.getUser().getId());
    }

}
