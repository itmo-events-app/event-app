package org.itmo.eventapp.main.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserShortDataResponse(@Schema(example = "1")
                                    Integer id,
                                    @Schema(example = "Иван")
                                    String name,
                                    @Schema(example = "Иванов")
                                    String surname) {
}
