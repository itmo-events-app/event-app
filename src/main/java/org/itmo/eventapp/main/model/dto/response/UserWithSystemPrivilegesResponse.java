package org.itmo.eventapp.main.model.dto.response;

import java.util.List;

public record UserWithSystemPrivilegesResponse(
        String name,
        String surname,
        List<PrivilegeResponse> privilegeResponses) {
}
