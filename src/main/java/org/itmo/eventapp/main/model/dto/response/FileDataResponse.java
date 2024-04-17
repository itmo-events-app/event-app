package org.itmo.eventapp.main.model.dto.response;

public record FileDataResponse(String filename,
                               String presignedUrl,
                               String unsignedUrl) {
}
