package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserChangePasswordRequest(
        @NotBlank(message = "Поле обязательно для заполнения")
        @Schema(example = "123password")
        String oldPassword,

        //TODO добавить @Password аннотацию
        @NotBlank(message = "Поле обязательно для заполнения")
        @Size(min = 8, message = "Минимальная длина пароля - 8 символов")
        @Schema(example = "123passwordNEW")
        String newPassword,

        @NotBlank(message = "Поле обязательно для заполнения")
        @Schema(example = "123passwordNEW")
        String confirmNewPassword
) {
}
