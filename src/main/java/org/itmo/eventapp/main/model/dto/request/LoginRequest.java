package org.itmo.eventapp.main.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.itmo.eventapp.main.model.validation.annotation.StrongPassword;


public record LoginRequest(
    @NotBlank(message = "Поле обязательно для заполнения")
    @Pattern(regexp = "^\\w[\\w\\-.]*@(niu|idu.)?itmo\\.ru$",
            message = "Должен использоваться email с доменом Университета ИТМО (@itmo.ru, @idu.itmo.ru или @niuitmo.ru)")
    String login,

    @NotBlank(message = "Поле обязательно для заполнения")
    @StrongPassword
    String password
) {}
