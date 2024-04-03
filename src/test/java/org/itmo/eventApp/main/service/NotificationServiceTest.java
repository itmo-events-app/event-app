package org.itmo.eventApp.main.service;

import org.itmo.eventApp.main.controller.AbstractTestContainers;
import org.itmo.eventapp.main.model.entity.Notification;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.repository.NotificationRepository;
import org.itmo.eventapp.main.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationServiceTest extends AbstractTestContainers {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void updateSeenWithCorrectDataTest(){
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_notifications.sql");

        User dummyUser = new User();
        Integer userId = 1;
        dummyUser.setId(userId);
        Integer notificationId = 1;
        notificationService.updateToSeen(notificationId);
        Notification afterUpdate = notificationRepository.findById(notificationId).get();

        assertTrue(afterUpdate.isSeen());
        assertNotNull(afterUpdate.getReadTime());
    }

    @Test
    void getAllByUserIdWithCorrectDataTest(){
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_notifications.sql");

        User dummyUser = new User();
        Integer userId = 1;
        dummyUser.setId(userId);

        ArrayList<Notification> notifications = (ArrayList<Notification>) notificationService.getAllByUserId(dummyUser);

        assertFalse(notifications.isEmpty());

        for (Notification n : notifications) {
            assertEquals(userId, n.getUser().getId());
        }
    }

    @Test
    void seenAllByUserIdWithCorrectDataTest(){
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_notifications.sql");

        User dummyUser = new User();
        Integer userId = 1;
        dummyUser.setId(userId);

        notificationService.updateSeenToAllByUserId(dummyUser);

        ArrayList<Notification> notifications = (ArrayList<Notification>) notificationService.getAllByUserId(dummyUser);

        assertFalse(notifications.isEmpty());

        for (Notification n : notifications) {
            assertEquals(userId, n.getUser().getId());
            assertTrue(n.isSeen());
            assertNotNull(n.getReadTime());
        }
    }

    @Test
    void createNotificationWithCorrectDataTest(){
        executeSqlScript("/sql/insert_user.sql");

        User dummyUser = new User();
        Integer userId = 1;
        dummyUser.setId(userId);
        String title = "TestTitle";
        String description = "TestDescription";
        Integer notificationId = 1;

        notificationService.createNotification(title, description, dummyUser);

        Notification afterCreate = notificationRepository.findById(notificationId).get();

        assertEquals(notificationId, afterCreate.getId());
        assertEquals(userId, afterCreate.getUser().getId());
        assertEquals(title, afterCreate.getTitle());
        assertEquals(description, afterCreate.getDescription());
        assertFalse(afterCreate.isSeen());
        assertNull(afterCreate.getReadTime());
    }

    @Test
    void deleteNotificationWithCorrectDataTest(){
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_notifications.sql");

        Integer notificationId = 1;

        assertTrue(notificationRepository.findById(notificationId).isPresent());

        notificationService.deleteNotification(notificationId);

        assertTrue(notificationRepository.findById(notificationId).isEmpty());

    }
}
