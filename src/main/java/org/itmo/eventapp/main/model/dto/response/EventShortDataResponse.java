package org.itmo.eventapp.main.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record EventShortDataResponse(
        @Schema(example = "1")
        Integer eventId,
        @Schema(example = "1")
        Integer activityId,
        @Schema(example = "День первокурсника")
        String eventTitle,
        @Schema(example = "Конкурс красоты первокурсников 'Сладенький пупс, держи мой чупа-чупс'")
        String activityTitle
) {



}
