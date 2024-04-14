package org.itmo.eventapp.main.model.dto.response;

import java.util.List;

public record PrivilegeWithHasOrganizerRolesResponse(
    List<PrivilegeResponse> privileges,

    boolean hasOrganizerRolesResponse) {
}
