package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RoleRequest(
    @NotBlank(message = "Название роли обязательно")
    @Size(message = "Название роли должно содержать от 1 до 256 символов", min = 1, max = 256)
    @Schema(example = "Администратор")
    String name,
    @NotBlank(message = "Описание роли обязательно")
    @Schema(example = "Администратор системы")
    String description,
    @NotNull(message = "Поле isEvent не может быть null")
    Boolean isEvent,
    @NotNull(message = "Поле privileges не может быть null")
    List<Integer> privileges
) {
}
