package org.itmo.eventapp.main.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record PlaceShortDataResponse(@Schema(example = "1")
                                     Integer id,
                                     @Schema(example = "Университет ИТМО")
                                     String name,
                                     @Schema(example = "Кронверкский пр., 49")
                                     String address) {
}
