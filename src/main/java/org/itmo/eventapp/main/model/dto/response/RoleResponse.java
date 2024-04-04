package org.itmo.eventapp.main.model.dto.response;

import org.itmo.eventapp.main.model.entity.enums.RoleType;

import java.util.List;

public record RoleResponse(Integer id,
                           String name,
                           String description,
                           RoleType type,
                           List<PrivilegeResponse> privileges) {
}