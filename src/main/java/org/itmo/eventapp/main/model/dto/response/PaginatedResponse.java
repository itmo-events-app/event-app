package org.itmo.eventapp.main.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PaginatedResponse<T>(
    @Schema(example = "1")
    long total,
    List<T> items) {

}
