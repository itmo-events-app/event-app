package org.itmo.eventapp.main.model.dto.request;

public record NotificationSettingsRequest(
        boolean enableEmail,
        boolean enablePush
) {
}
