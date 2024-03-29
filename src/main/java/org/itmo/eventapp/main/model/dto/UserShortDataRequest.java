package org.itmo.eventapp.main.model.dto;

public record UserShortDataRequest(Integer id,
                               String name,
                               String surname) {
}
