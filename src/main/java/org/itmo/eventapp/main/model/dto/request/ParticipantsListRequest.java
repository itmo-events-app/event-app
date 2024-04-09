package org.itmo.eventapp.main.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.File;

public record ParticipantsListRequest (

        @NotEmpty(message = "Файл не может быть пустым")
        File participantsListFile
       ) {
}
