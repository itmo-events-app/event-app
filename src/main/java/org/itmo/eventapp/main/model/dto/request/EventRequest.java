package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.itmo.eventapp.main.model.entity.enums.EventFormat;
import org.itmo.eventapp.main.model.entity.enums.EventStatus;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

// TODO: Add correct annotations
public record EventRequest(
    @Min(value = 1, message = "Поле eventId не может быть меньше 1!")
    @NotNull(message = "Поле placeId не может быть null!")
    @Schema(example = "67", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer placeId,
    @FutureOrPresent
    @NotNull(message = "Поле startDate не может быть null!")
    @Schema(example = "2024-09-01Е12:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDateTime startDate,
    @Future
    @NotNull(message = "Поле endDate не может быть null!")
    @Schema(example = "2024-09-01Е17:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDateTime endDate,
    @NotBlank(message = "Поле title не может быть пустой!")
    @Schema(example = "День первокурсника", requiredMode = Schema.RequiredMode.REQUIRED)
    String title,
    @NotBlank(message = "Поле shortDescription не может быть пустой!")
    @Schema(example = "День для всех первокурсников", requiredMode = Schema.RequiredMode.REQUIRED)
    String shortDescription,
    @NotNull(message = "Поле fullDescription не может быть null!")
    @Schema(example = "День для всех первокурсников, где они познакомятся с университетом ИТМО", requiredMode = Schema.RequiredMode.REQUIRED)
    String fullDescription,
    @NotNull(message = "Поле format не может быть null!")
    @Schema(example = "OFFLINE", requiredMode = Schema.RequiredMode.REQUIRED)
    EventFormat format,
    @NotNull(message = "Поле status не может быть null!")
    @Schema(example = "PUBLISHED", requiredMode = Schema.RequiredMode.REQUIRED)
    EventStatus status,
    @FutureOrPresent
    @NotNull(message = "Поле registrationStart не может быть null!")
    @Schema(example = "2024-08-01Е12:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDateTime registrationStart,
    @Future
    @NotNull(message = "Поле registrationEnd не может быть null!")
    @Schema(example = "2024-08-31Е12:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDateTime registrationEnd,
    @Schema(example = "123", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    Integer parent,
    @NotNull(message = "Поле participantLimit не может быть null!")
    @Min(value = 1, message = "Поле participantLimit не может быть меньше 1!")
    @Schema(example = "12000", requiredMode = Schema.RequiredMode.REQUIRED)
    int participantLimit,
    @NotNull(message = "Поле participantAgeLowest не может быть null!")
    @Min(value = 1, message = "Поле participantAgeLowest не может быть меньше 1!")
    @Max(value = 150, message = "Поле participantAgeHighest не может быть больше 150!")
    @Schema(example = "16", requiredMode = Schema.RequiredMode.REQUIRED)
    int participantAgeLowest,
    @NotNull(message = "Поле participantAgeHighest не может быть null!")
    @Min(value = 1, message = "Поле participantAgeHighest не может быть меньше 1!")
    @Max(value = 150, message = "Поле participantAgeHighest не может быть больше 150!")
    @Schema(example = "30", requiredMode = Schema.RequiredMode.REQUIRED)
    int participantAgeHighest,
    @Future
    @NotNull(message = "Поле preparingStart не может быть null!")
    @Schema(example = "2024-08-01Е12:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDateTime preparingStart,
    @Future
    @NotNull(message = "Поле preparingEnd не может быть null!")
    @Schema(example = "2024-08-31Е12:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDateTime preparingEnd,
    @Schema(example = "image.png", requiredMode = Schema.RequiredMode.REQUIRED)
    MultipartFile image
) {
}
