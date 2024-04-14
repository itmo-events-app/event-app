package org.itmo.eventapp.main.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.itmo.eventapp.main.model.entity.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

public record TaskResponse(
        @Schema(example = "1")
        Integer id,
        EventShortDataResponse event,
        @Schema(example = "Настроить проектор")
        String title,
        @Schema(example = "Настроить проектор в коворкинге")
        String description,
        TaskStatus taskStatus,
        UserShortDataResponse assignee,
        PlaceShortDataResponse place,
        LocalDateTime creationTime,
        LocalDateTime deadline,
        LocalDateTime reminder,
        List<FileDataResponse> fileData
) {
}