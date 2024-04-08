package org.itmo.eventapp.main.model.dto.request;

import org.itmo.eventapp.main.model.entity.enums.LoginType;

public interface CommonLoginRequest {
    String login();
    LoginType type();
}
