package org.itmo.eventapp.main.model.dto;

import lombok.*;

@Builder
public record RoleDto(Integer id,
                      String name,
                      String description) {
}