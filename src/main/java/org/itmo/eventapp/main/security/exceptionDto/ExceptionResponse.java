package org.itmo.eventapp.main.security.exceptionDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionResponse {
    private int statusCode;
    private String description;
}
