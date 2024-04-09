package org.itmo.eventapp.main.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.itmo.eventapp.main.model.validation.annotation.PasswordMatching;
import org.itmo.eventapp.main.model.validation.annotation.StrongPassword;

@PasswordMatching()
public record RegistrationUserRequest(

    @NotBlank(message = "Поле обязательно для заполнения")
    @Schema(example = "Иван", requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @NotBlank(message = "Поле обязательно для заполнения")
    @Schema(example = "Иванов", requiredMode = Schema.RequiredMode.REQUIRED)
    String surname,

    @Pattern(regexp = "^\\w[\\w\\-.]*@(niu|idu.)?itmo\\.ru$",
            message = "Должен использоваться email с доменом Университета ИТМО (@itmo.ru, @idu.itmo.ru или @niuitmo.ru)")
    @Schema(example = "333666@niuitmo.ru", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Поле обязательно для заполнения")
    String email,

    @NotBlank(message = "Поле обязательно для заполнения")
    @StrongPassword
    @Schema(example = "PaSsWoRd1!", requiredMode = Schema.RequiredMode.REQUIRED)
    String password,

    @NotBlank(message = "Поле обязательно для заполнения")
    @Schema(example = "PaSsWoRd1!", requiredMode = Schema.RequiredMode.REQUIRED)
    String confirmPassword
){}
