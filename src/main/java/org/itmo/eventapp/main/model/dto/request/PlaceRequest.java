package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.itmo.eventapp.main.model.entity.enums.PlaceFormat;

public record PlaceRequest(
        @NotBlank(message = "Поле name не может быть пустым!")
        @Schema(example = "Университет ИТМО")
        String name,
        @NotBlank(message = "Поле address не может быть пустым!")
        @Schema(example = "Кронверкский, 49")
        String address,
        @NotNull(message = "Поле format не может быть null!")
        PlaceFormat format,
        @NotNull(message = "Поле room не может быть null!")
        @Schema(example = "ауд. 2304")
        String room,
        @NotNull(message = "Поле description не может быть null!")
        @Schema(example = "Комната переговоров")
        String description,
        @NotNull
        @DecimalMin(value = "-90.0", message = "Поле Latitude должно быть больше или равно -90.0!")
        @DecimalMax(value = "90.0", message = "Поле Latitude должно быть меньше или равно 90.0!")
        @Schema(example = "85.234")
        Float latitude,
        @NotNull
        @DecimalMin(value = "-180.0", message = "Поле Longitude должно быть больше или равно -180.0!")
        @DecimalMax(value = "180.0", message = "Поле Longitude должно быть меньше или равно 180.0!")
        @Schema(example = "150.234")
        Float longitude
) {
}
