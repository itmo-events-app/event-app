package org.itmo.eventapp.main.model.dto.response;

import java.util.List;

/**
 * For Swagger
 * @param content
 * @param totalPages
 * @param totalElements
 * @param last
 * @param first
 * @param number
 */
public record NotificationPageResponse (
    List<NotificationResponse> content,
    int totalPages,
    long totalElements,
    boolean last,
    boolean first,
    int number
) {}
