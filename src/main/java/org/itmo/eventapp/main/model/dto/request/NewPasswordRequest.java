package org.itmo.eventapp.main.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.itmo.eventapp.main.model.validation.annotation.PasswordMatching;
import org.itmo.eventapp.main.model.validation.annotation.StrongPassword;

@PasswordMatching(
        password = "newPassword",
        confirmPassword = "confirmNewPassword"
)
public record NewPasswordRequest(

        String token,

        @NotBlank(message = "Поле не должно быть пустым")
        @StrongPassword
        String newPassword,

        @NotBlank(message = "Поле не должно быть пустым")
        String confirmNewPassword
) {}
