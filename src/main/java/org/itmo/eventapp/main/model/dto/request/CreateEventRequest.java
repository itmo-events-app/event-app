package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateEventRequest(
    @NotNull
    @Min(value = 1, message = "Поле userId не может быть меньше 1!")
    @Schema(example = "3242", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer userId,
    @NotBlank(message = "Поле title не может быть пустой!")
    @Schema(example = "День первокурсника", requiredMode = Schema.RequiredMode.REQUIRED)
    String title
) {
}
