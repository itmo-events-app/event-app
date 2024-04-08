package org.itmo.eventapp.main.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeName;

public record PrivilegeResponse(
        @Schema(example = "1")
        Integer id,
        @Schema(example = "APPROVE_REGISTRATION_REQUEST")
        PrivilegeName name,
        @Schema(example = "Одобрение заявок на регистрацию")
        String description) {
}
