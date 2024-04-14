package org.itmo.eventApp.main.service;

import org.itmo.eventApp.main.controller.AbstractTestContainers;
import org.itmo.eventapp.main.model.entity.Notification;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.repository.NotificationRepository;
import org.itmo.eventapp.main.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest extends AbstractTestContainers {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    private void databaseFilling() {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_notifications.sql");
    }

    @Test
    void updateSeenWithCorrectDataTest() {
        databaseFilling();

        User dummyUser = new User();
        Integer userId = 1;
        dummyUser.setId(userId);
        Integer notificationId = 1;

        Notification afterUpdate = notificationService.updateToSeen(notificationId, userId);

        assertTrue(afterUpdate.isSeen());
        assertNotNull(afterUpdate.getSentTime());
    }

    @Test
    void getAllByUserIdWithCorrectDataTest() {
        databaseFilling();

        Integer userId = 1;
        Integer page = 0;
        Integer size = 1;

        ArrayList<Notification> notifications = (ArrayList<Notification>) notificationService.getAllByUserId(userId, page, size);

        assertFalse(notifications.isEmpty());

        for (Notification n : notifications) {
            assertEquals(userId, n.getUser().getId());
        }

        assertEquals(1, notifications.size());
    }

    @Test
    void seenAllByUserIdWithCorrectDataTest() {
        databaseFilling();

        Integer userId = 1;
        Integer page = 0;
        Integer size = 1;

        ArrayList<Notification> notifications = (ArrayList<Notification>) notificationService.updateSeenToAllByUserId(userId, page, size);

        assertFalse(notifications.isEmpty());

        for (Notification n : notifications) {
            assertEquals(userId, n.getUser().getId());
            assertTrue(n.isSeen());
        }
    }

    @Test
    void createNotificationWithCorrectDataTest() {
        databaseFilling();

        Integer userId = 1;
        String title = "TestTitle";
        String description = "TestDescription";
        Integer notificationId = 5;
        String link = "http://localhost:8080/task/1";

        notificationService.createNotification(title, description, userId, link);

        Notification afterCreate = notificationRepository.findById(notificationId).get();

        assertEquals(notificationId, afterCreate.getId());
        assertEquals(userId, afterCreate.getUser().getId());
        assertEquals(title, afterCreate.getTitle());
        assertEquals(description, afterCreate.getDescription());
        assertFalse(afterCreate.isSeen());
        assertNotNull(afterCreate.getSentTime());
        assertEquals(link, afterCreate.getLink());
    }

    @Test
    void deleteNotificationWithCorrectDataTest() {
        databaseFilling();

        Integer notificationId = 1;
        LocalDateTime beforeTime = LocalDateTime.of(2025, 1, 1, 0, 0);

        assertTrue(notificationRepository.findById(notificationId).isPresent());

        notificationService.deleteNotificationsBeforeSentTime(beforeTime);

        assertTrue(notificationRepository.findById(notificationId).isEmpty());

    }
}
