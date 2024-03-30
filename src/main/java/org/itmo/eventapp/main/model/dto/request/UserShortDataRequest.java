package org.itmo.eventapp.main.model.dto.request;

public record UserShortDataRequest(Integer id,
                               String name,
                               String surname) {
}
