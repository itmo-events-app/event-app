package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserChangeNameRequest(
        @NotBlank(message = "Имя не может быть пустым")
        @Pattern(regexp = "^[а-яА-ЯёЁ]+$", message = "Имя должно содержать только буквы кириллицы без цифр и специальных символов")
        @Schema(example = "Лидия")
        String name,

        @NotBlank(message = "Фамилия не может быть пустой")
        @Pattern(regexp = "^[а-яА-ЯёЁ]+$", message = "Фамилия должна содержать только буквы кириллицы без цифр и специальных символов")
        @Schema(example = "Петрова")
        String surname
) {
}
