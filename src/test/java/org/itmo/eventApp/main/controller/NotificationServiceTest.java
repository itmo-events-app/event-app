package org.itmo.eventApp.main.controller;

import org.itmo.eventapp.main.model.entity.Notification;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationServiceTest extends AbstractTestContainers{

    @Autowired
    private NotificationService notificationService;

    @Test
    void updateSeenTest(){
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_notifications.sql");

        User dummyUser = new User();
        Integer userId = 1;
        dummyUser.setId(userId);
        Integer notificationId = 1;

        Notification afterUpdate = notificationService.updateToSeen(notificationId);

        assertTrue(afterUpdate.isSeen());
        assertNotNull(afterUpdate.getReadTime());
    }

    @Test
    void getAllByUserId(){
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
    void seenAllByUserId(){
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
}
