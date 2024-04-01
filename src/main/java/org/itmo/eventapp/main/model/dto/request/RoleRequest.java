package org.itmo.eventapp.main.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record RoleRequest(
        @NotBlank(message = "Название роли обязательно")
        @Length(message = "Название роли должно содержать от 1 до 256 символов", min = 1, max = 256)
        String name,
        @NotBlank(message = "Описание роли обязательно")
        String description,
        Boolean isEvent,
        List<Integer> privileges
) {

}
