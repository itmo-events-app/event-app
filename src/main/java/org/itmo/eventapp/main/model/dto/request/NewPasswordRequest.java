package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.itmo.eventapp.main.model.validation.annotation.PasswordMatching;
import org.itmo.eventapp.main.model.validation.annotation.StrongPassword;

@SuppressWarnings("java:S2068")
@PasswordMatching(
        password = "newPassword",
        confirmPassword = "confirmNewPassword"
)
public record NewPasswordRequest(

        @Schema(example = "c5b7bcc0-cffe-4f57-853c-7fa18e56b36d", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String token,

        @NotBlank(message = "Поле пароля не должно быть пустым")
        @StrongPassword
        @Schema(example = "PaSsWoRd1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String newPassword,

        @NotBlank(message = "Поле подтверждения пароля не должно быть пустым")
        @Schema(example = "PaSsWoRd1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String confirmNewPassword
) {}
