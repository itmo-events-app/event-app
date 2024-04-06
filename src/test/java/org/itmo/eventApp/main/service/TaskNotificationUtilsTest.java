package org.itmo.eventApp.main.service;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.itmo.eventApp.main.controller.AbstractTestContainers;
import org.itmo.eventapp.main.model.entity.*;
import org.itmo.eventapp.main.repository.NotificationRepository;
import org.itmo.eventapp.main.util.TaskNotificationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.io.IOException;
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
                        .userLoginInfo(UserLoginInfo.builder().email("test_mail@itmo.ru").build()).build())
                .assigner(User.builder().id(2).name("OrganizerName")
                        .userLoginInfo(UserLoginInfo.builder().email("test_mail1@itmo.ru").build()).build())
                .build();
    }

    @Test
    void correctCreateIncomingTaskNotificationTest() throws Exception {
        databaseFilling();

        taskNotificationUtils.createIncomingTaskNotification(createTaskForNotification());

        String expectedMessage = """
                <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
                <p style="font-weight: 600">Здравствуйте, HelperName!</p>
                <p>Вам на выполнение поступила новая задача - <a href="http://localhost:8080/task/1">TestTask</a> в мероприятии EventTitle, можете приступить к выполнению!</p>""";
        String expectedTitle = "Новая задача!";
        String expectedDescription = "Вам назначена новая задача - TestTask в мероприятии EventTitle";

        Awaitility.await().atMost(5, TimeUnit.SECONDS).until(() -> greenMail.getReceivedMessages().length == 1);

        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
        assertEquals(1, receivedMessage.getAllRecipients().length);
        assertEquals("test_mail@itmo.ru", receivedMessage.getAllRecipients()[0].toString());
        assertEquals("sender@test", receivedMessage.getFrom()[0].toString());
        assertEquals("Новая задача!", receivedMessage.getSubject());
        // replace \r\n over \n to resolve test conflicts on Windows and Linux
        assertEquals(expectedMessage.replace("\r\n", "\n"), receivedMessage.getContent().toString().replace("\r\n", "\n"));

        Notification notification = notificationRepository.findById(1).orElseThrow(() ->new Exception("NotificationNotFound"));

        assertEquals(expectedTitle, notification.getTitle());
        assertEquals(expectedDescription, notification.getDescription());
        assertNotNull(notification.getSentTime());
        assertFalse(notification.isSeen());
        assertEquals(1, notification.getUser().getId());
    }

}
