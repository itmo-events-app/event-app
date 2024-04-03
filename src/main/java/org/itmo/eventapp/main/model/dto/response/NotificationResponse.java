package org.itmo.eventapp.main.model.dto.response;

public record NotificationResponse(
        Integer id,
        String title,
        String description,
        boolean seen
) {
}
