package org.itmo.eventapp.main.model.dto.response;

public record UserRoleResponse(Integer id,
                               String name,
                               String surname,
                               String roleName) {
}
