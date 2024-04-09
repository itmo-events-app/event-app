package org.itmo.eventapp.main.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserRoleResponse(@Schema(example = "13")
                               Integer id,
                               @Schema(example = "Иван")
                               String name,
                               @Schema(example = "Иванов")
                               String surname,
                               @Schema(example = "test_mail@itmo.ru")
                               String login,
                               @Schema(example = "Организатор")
                               String roleName) {
}
