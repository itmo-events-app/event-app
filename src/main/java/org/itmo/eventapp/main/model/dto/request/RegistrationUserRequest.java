package org.itmo.eventapp.main.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.itmo.eventapp.main.model.validation.annotation.PasswordMatching;
import org.itmo.eventapp.main.model.validation.annotation.StrongPassword;

@PasswordMatching()
public record RegistrationUserRequest(

    @NotBlank(message = "Поле обязательно для заполнения")
    String name,

    @NotBlank(message = "Поле обязательно для заполнения")
    String surname,

    @Pattern(regexp = "^\\w[\\w\\-.]*@(niu|idu.)?itmo\\.ru$",
            message = "Должен использоваться email с доменом Университета ИТМО (@itmo.ru, @idu.itmo.ru или @niuitmo.ru)")
    @NotBlank(message = "Поле обязательно для заполнения")
    String email,

    @NotBlank(message = "Поле обязательно для заполнения")
    @StrongPassword
    String password,

    @NotBlank(message = "Поле обязательно для заполнения")
    String confirmPassword
){}
