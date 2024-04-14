package org.itmo.eventapp.main.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;


public record ParticipantResponse(

    @Schema(example = "67", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer id,

    @Schema(example = "Иванов Иван Иванович", requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @Schema(example = "email@niu.itmo", requiredMode = Schema.RequiredMode.REQUIRED)
    String email,
    @Schema(example = "+78653452676")
    String additionalInfo,

    @Schema(example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    boolean visited,

    @Schema(example = "34", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer eventId
) {
}
