package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

// TODO, id or username (email in out case)?
public record UserShortDataRequest(
        @Schema(example = "1")
        Integer id,
        @Schema(example = "Иван")
        String name,
        @Schema(example = "Иванов")
        String surname) {
}
