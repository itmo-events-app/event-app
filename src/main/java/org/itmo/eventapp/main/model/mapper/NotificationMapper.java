package org.itmo.eventapp.main.model.mapper;

import org.itmo.eventapp.main.model.dto.response.NotificationResponse;
import org.itmo.eventapp.main.model.entity.Notification;

public class NotificationMapper {

    private NotificationMapper(){
    }

    public static NotificationResponse notificationToNotificationResponse(Notification notification){
        return new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getDescription(),
                notification.isSeen());
    }
}