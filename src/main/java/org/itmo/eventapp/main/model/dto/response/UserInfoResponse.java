package org.itmo.eventapp.main.model.dto.response;

import org.itmo.eventapp.main.model.entity.enums.LoginType;

public record UserInfoResponse(
        String login,
        LoginType type
) {
}
