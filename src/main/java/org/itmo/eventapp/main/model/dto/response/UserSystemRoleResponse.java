package org.itmo.eventapp.main.model.dto.response;

import org.itmo.eventapp.main.model.entity.enums.LoginType;

public record UserSystemRoleResponse (
            String login,

            LoginType type,

            String name,

            String surname,

            String role
) {
}
