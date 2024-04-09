package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.itmo.eventapp.main.model.entity.enums.LoginType;
import org.itmo.eventapp.main.model.validation.annotation.ValidLogin;

@ValidLogin()
public record UserChangeLoginRequest(
        @Schema(example = "333111@niuitmo.ru")
        String login,

        @Schema(example = "EMAIL", requiredMode = Schema.RequiredMode.REQUIRED)
        LoginType type
) {}
