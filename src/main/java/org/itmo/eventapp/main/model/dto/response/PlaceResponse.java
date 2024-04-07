package org.itmo.eventapp.main.model.dto.response;

import org.itmo.eventapp.main.model.entity.enums.PlaceFormat;

public record PlaceResponse(
        Integer id,
        String name,
        String address,
        PlaceFormat format,
        String room,
        String description,
        Float latitude,
        Float longitude,
        String renderInfo
) {
}