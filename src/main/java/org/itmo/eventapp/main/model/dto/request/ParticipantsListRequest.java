package org.itmo.eventapp.main.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public record ParticipantsListRequest(
    @NotEmpty(message = "Файл не может быть пустым")
    MultipartFile participantsListFile
) {
}
