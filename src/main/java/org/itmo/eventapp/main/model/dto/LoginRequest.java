package org.itmo.eventapp.main.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record LoginRequest(
    @NotBlank(message = "Поле обязательно для заполнения")
    @Email()
    String login,

    @NotBlank(message = "Поле обязательно для заполнения")
    @Size(min = 8, message = "Минимальная длина пароля - 8 символов")
    String password
) {}
