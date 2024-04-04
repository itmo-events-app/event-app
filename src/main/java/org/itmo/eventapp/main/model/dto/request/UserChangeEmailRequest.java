package org.itmo.eventapp.main.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserChangeEmailRequest(
        @NotBlank(message = "Email не может быть пустым")
        @Pattern(regexp = "^\\w[\\w\\-\\.]*@(niu|idu.)?itmo\\.ru$", message = "Некорректный email. Поддерживаемые домены: @itmo.ru, @idu.itmo.ru и @niuitmo.ru")
        String email
) {
}
