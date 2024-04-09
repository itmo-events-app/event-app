package org.itmo.eventapp.main.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record NotificationResponse(
        Integer id,
        @Schema(example = "Ваша задача просрочнена")
        String title,
        @Schema(example = "Ваша задача 'Провести инструктаж' к мероприятию 'День первокурсника' просрочнена")
        String description,
        boolean seen,
        LocalDateTime sent_time
) {
}
