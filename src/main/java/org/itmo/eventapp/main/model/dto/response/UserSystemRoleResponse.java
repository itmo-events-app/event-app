package org.itmo.eventapp.main.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.itmo.eventapp.main.model.entity.enums.LoginType;

import java.util.List;

public record UserSystemRoleResponse(
        Integer id,
        @Schema(example = "Иван")
    String name,
        @Schema(example = "Иванов")
    String surname,
        @Schema(example = "ivan@itmo.ru")
    String login,
        @Schema(example = "EMAIL")
    LoginType type,
//    @Schema(example = "Читатель")
    List<String> roles
) {
}
