package org.itmo.eventapp.main.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.itmo.eventapp.main.model.entity.enums.PlaceFormat;

public record PlaceResponse(
        @Schema(example = "1")
        Integer id,
        @Schema(example = "Университет ИТМО")
        String name,
        @Schema(example = "Кронверкский пр., 49")
        String address,
        PlaceFormat format,
        @Schema(example = "ауд. 2304")
        String room,
        @Schema(example = "Комната переговоров")
        String description,
        @Schema(example = "85.234")
        Float latitude,
        @Schema(example = "150.234")
        Float longitude,
        String renderInfo
) {
}