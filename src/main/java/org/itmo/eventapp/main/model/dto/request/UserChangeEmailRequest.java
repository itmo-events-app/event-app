package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserChangeEmailRequest(
        @NotBlank(message = "Email не может быть пустым")
        @Pattern(regexp = "^\\w[\\w\\-\\.]*@(niu|idu.)?itmo\\.ru$", message = "Некорректный email. Поддерживаемые домены: @itmo.ru, @idu.itmo.ru и @niuitmo.ru")
        @Schema(example = "333111@niuitmo.ru")
        String email
) {
}
