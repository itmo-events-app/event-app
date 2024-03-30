package org.itmo.eventapp.main.model.dto.request;

// TODO, id or username (email in out case)?
public record UserShortDataRequest(Integer id,
                               String name,
                               String surname) {
}
