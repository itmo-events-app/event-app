package org.itmo.eventapp.main.model.dto.request;

import jakarta.validation.constraints.NotBlank;


public record LoginRequest(
    @NotBlank(message = "Поле обязательно для заполнения")
    String login,

    @NotBlank(message = "Поле обязательно для заполнения")
    String password
) {}
