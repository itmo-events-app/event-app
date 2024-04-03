package org.itmo.eventapp.main.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserChangeNameRequest(
        @NotBlank(message = "Имя не может быть пустым")
        @Pattern(regexp = "^[а-яА-ЯёЁ]+$", message = "Имя должно содержать только буквы кириллицы без цифр и специальных символов")
        String name,

        @NotBlank(message = "Фамилия не может быть пустой")
        @Pattern(regexp = "^[а-яА-ЯёЁ]+$", message = "Фамилия должна содержать только буквы кириллицы без цифр и специальных символов")
        String surname
) {
}
