package org.itmo.eventapp.main.model.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.itmo.eventapp.main.model.entity.enums.EventFormat;
import org.itmo.eventapp.main.model.entity.enums.EventStatus;

import java.time.LocalDateTime;

// TODO: Add correct annotations
public record EventRequest(
        @Min(value = 1, message = "Поле eventId не может быть меньше 1!")
        Integer placeId,
        LocalDateTime start,
        LocalDateTime end,
        @NotBlank(message = "Поле title не может быть пустой!")
        String title,
        String shortDescription,
        String fullDescription,
        EventFormat format,
        EventStatus status,
        LocalDateTime registrationStart,
        LocalDateTime registrationEnd,
        Integer parent,
        @Min(value = 1, message = "Поле participantLimit не может быть меньше 1!")
        int participantLimit,
        @Min(value = 1, message = "Поле participantAgeLowest не может быть меньше 1!")
        @Max(value = 150, message = "Поле participantAgeHighest не может быть больше 150!")
        int participantAgeLowest,
        @Min(value = 1, message = "Поле participantAgeHighest не может быть меньше 1!")
        @Max(value = 150, message = "Поле participantAgeHighest не может быть больше 150!")
        int participantAgeHighest,
        LocalDateTime preparingStart,
        LocalDateTime preparingEnd
) {
}
