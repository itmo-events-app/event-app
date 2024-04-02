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
        @NotNull(message = "Поле placeId не может быть null!")
        Integer placeId,
        @NotNull(message = "Поле startDate не может быть null!")
        LocalDateTime startDate,
        @NotNull(message = "Поле endDate не может быть null!")
        LocalDateTime endDate,
        @NotBlank(message = "Поле title не может быть пустой!")
        String title,
        @NotBlank(message = "Поле shortDescription не может быть пустой!")
        String shortDescription,
        @NotNull(message = "Поле fullDescription не может быть null!")
        String fullDescription,
        @NotNull(message = "Поле format не может быть null!")
        EventFormat format,
        @NotNull(message = "Поле status не может быть null!")
        EventStatus status,
        @NotNull(message = "Поле registrationStart не может быть null!")
        LocalDateTime registrationStart,
        @NotNull(message = "Поле registrationEnd не может быть null!")
        LocalDateTime registrationEnd,
        Integer parent,
        @NotNull(message = "Поле participantLimit не может быть null!")
        @Min(value = 1, message = "Поле participantLimit не может быть меньше 1!")
        int participantLimit,
        @NotNull(message = "Поле participantAgeLowest не может быть null!")
        @Min(value = 1, message = "Поле participantAgeLowest не может быть меньше 1!")
        @Max(value = 150, message = "Поле participantAgeHighest не может быть больше 150!")
        int participantAgeLowest,
        @NotNull(message = "Поле participantAgeHighest не может быть null!")
        @Min(value = 1, message = "Поле participantAgeHighest не может быть меньше 1!")
        @Max(value = 150, message = "Поле participantAgeHighest не может быть больше 150!")
        int participantAgeHighest,
        @NotNull(message = "Поле preparingStart не может быть null!")
        LocalDateTime preparingStart,
        @NotNull(message = "Поле preparingEnd не может быть null!")
        LocalDateTime preparingEnd

) {
}
