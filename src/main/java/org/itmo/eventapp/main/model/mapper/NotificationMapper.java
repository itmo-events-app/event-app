package org.itmo.eventapp.main.model.mapper;

import org.itmo.eventapp.main.model.dto.response.NotificationResponse;
import org.itmo.eventapp.main.model.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

public final class NotificationMapper {

    private NotificationMapper(){
    }

    public static NotificationResponse notificationToNotificationResponse(Notification notification){
        return new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getDescription(),
                notification.isSeen(),
                notification.getSentTime(),
                notification.getLink());
    }

    public static Page<NotificationResponse> notificationPageToNotificationPageResponse(Page<Notification> page) {
        List<NotificationResponse> responseList = page.getContent().stream()
                .map(NotificationMapper::notificationToNotificationResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(responseList, page.getPageable(), page.getTotalElements());
    }
}
