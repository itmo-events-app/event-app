package org.itmo.eventapp.main.model.dto.response;

import lombok.*;

@Builder
public record RoleResponse(Integer id,
                           String name,
                           String description) {
}