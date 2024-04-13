package org.itmo.eventapp.main.model.dto.response;

public record TaskObjectResponse(Integer id,
                                 Integer taskId,
                                 String originalFileName) {
}
