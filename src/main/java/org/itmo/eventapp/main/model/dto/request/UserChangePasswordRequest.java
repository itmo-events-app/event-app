package org.itmo.eventapp.main.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserChangePasswordRequest(
        @NotBlank(message = "Поле обязательно для заполнения")
        String oldPassword,
        @NotBlank(message = "Поле обязательно для заполнения")
        @Size(min = 8, message = "Минимальная длина пароля - 8 символов")
        String newPassword,

        @NotBlank(message = "Поле обязательно для заполнения")
        String confirmNewPassword
) {
}
