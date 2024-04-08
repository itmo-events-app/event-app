package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.itmo.eventapp.main.model.entity.enums.LoginType;
import org.itmo.eventapp.main.model.validation.annotation.PasswordMatching;
import org.itmo.eventapp.main.model.validation.annotation.StrongPassword;
import org.itmo.eventapp.main.model.validation.annotation.ValidLogin;

@ValidLogin
@PasswordMatching()
public record RegistrationUserRequest(

    @NotBlank(message = "Поле обязательно для заполнения")
    @Schema(example = "Иван", requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @NotBlank(message = "Поле обязательно для заполнения")
    @Schema(example = "Иванов", requiredMode = Schema.RequiredMode.REQUIRED)
    String surname,

    @Schema(example = "333666@niuitmo.ru", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Поле обязательно для заполнения")
    String login,

    @Schema(example = "EMAIL", requiredMode = Schema.RequiredMode.REQUIRED)
    LoginType type,

    @NotBlank(message = "Поле обязательно для заполнения")
    @StrongPassword
    @Schema(example = "PaSsWoRd1!", requiredMode = Schema.RequiredMode.REQUIRED)
    String password,

    @NotBlank(message = "Поле обязательно для заполнения")
    @Schema(example = "PaSsWoRd1!", requiredMode = Schema.RequiredMode.REQUIRED)
    String confirmPassword
) implements CommonLoginRequest {}
