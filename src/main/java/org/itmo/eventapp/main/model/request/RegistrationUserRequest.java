package org.itmo.eventapp.main.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationUserRequest(

    @NotBlank(message = "Поле обязательно для заполнения")
    String name,

    @NotBlank(message = "Поле обязательно для заполнения")
    String surname,

    @NotBlank(message = "Поле обязательно для заполнения")
    String login,

    @Email(message = "Неверный формат email")
    @NotBlank(message = "Поле обязательно для заполнения")
    String email,

    @NotBlank(message = "Поле обязательно для заполнения")
    @Size(min = 8, message = "Минимальная длина пароля - 8 символов")
    String password,

    @NotBlank(message = "Поле обязательно для заполнения")
    String confirmPassword
){}
