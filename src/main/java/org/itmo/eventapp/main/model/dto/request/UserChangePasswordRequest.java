package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.itmo.eventapp.main.model.validation.annotation.StrongPassword;

public record UserChangePasswordRequest(
        @StrongPassword
        @NotBlank(message = "Поле обязательно для заполнения")
        @Schema(example = "123password")
        String oldPassword,

        @NotBlank(message = "Поле обязательно для заполнения")
        @StrongPassword
        @Schema(example = "123passwordNEW")
        String newPassword,

        @NotBlank(message = "Поле обязательно для заполнения")
        @Schema(example = "123passwordNEW")
        String confirmNewPassword
) {
}
