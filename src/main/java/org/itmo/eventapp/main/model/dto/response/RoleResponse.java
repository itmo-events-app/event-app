package org.itmo.eventapp.main.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.itmo.eventapp.main.model.entity.enums.RoleType;

import java.util.List;

public record RoleResponse(
        @Schema(example = "1")
        Integer id,
        @Schema(example = "Администратор")
        String name,
        @Schema(example = "Администратор системы")
        String description,
        @Schema(example = "SYSTEM")
        RoleType type,
        List<PrivilegeResponse> privileges) {
}