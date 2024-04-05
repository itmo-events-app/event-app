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

    private void databaseFilling(){
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_notifications.sql");
    }

    @Test
    void updateSeenWithCorrectDataTest(){
        databaseFilling();

        User dummyUser = new User();
        Integer userId = 1;
        dummyUser.setId(userId);
        Integer notificationId = 1;

        Notification afterUpdate = notificationService.updateToSeen(notificationId, dummyUser);

        assertTrue(afterUpdate.isSeen());
        assertNotNull(afterUpdate.getSentTime());
    }

    @Test
    void getAllByUserIdWithCorrectDataTest(){
        databaseFilling();

        User dummyUser = new User();
        Integer userId = 1;
        dummyUser.setId(userId);
        Integer page = 0;
        Integer size = 1;

        ArrayList<Notification> notifications = (ArrayList<Notification>) notificationService.getAllByUserId(dummyUser, page, size);

        assertFalse(notifications.isEmpty());

        for (Notification n : notifications) {
            assertEquals(userId, n.getUser().getId());
        }

        assertEquals(1, notifications.size());
    }

    @Test
    void seenAllByUserIdWithCorrectDataTest(){
        databaseFilling();

        User dummyUser = new User();
        Integer userId = 1;
        dummyUser.setId(userId);
        Integer page = 0;
        Integer size = 1;

        ArrayList<Notification> notifications = (ArrayList<Notification>) notificationService.updateSeenToAllByUserId(dummyUser, page, size);

        assertFalse(notifications.isEmpty());

        for (Notification n : notifications) {
            assertEquals(userId, n.getUser().getId());
            assertTrue(n.isSeen());
        }
    }

    @Test
    void createNotificationWithCorrectDataTest(){
        databaseFilling();

        User dummyUser = new User();
        Integer userId = 1;
        dummyUser.setId(userId);
        String title = "TestTitle";
        String description = "TestDescription";
        Integer notificationId = 5;

        notificationService.createNotification(title, description, dummyUser);

        Notification afterCreate = notificationRepository.findById(notificationId).get();

        assertEquals(notificationId, afterCreate.getId());
        assertEquals(userId, afterCreate.getUser().getId());
        assertEquals(title, afterCreate.getTitle());
        assertEquals(description, afterCreate.getDescription());
        assertFalse(afterCreate.isSeen());
        assertNotNull(afterCreate.getSentTime());
    }

    @Test
    void deleteNotificationWithCorrectDataTest(){
        databaseFilling();

        Integer notificationId = 1;

        assertTrue(notificationRepository.findById(notificationId).isPresent());

        notificationService.deleteNotification(notificationId);

        assertTrue(notificationRepository.findById(notificationId).isEmpty());

    }
}
