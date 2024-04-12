package org.itmo.eventapp.main.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeName;

import java.util.List;

public record PrivilegeWithHasOrganizerRolesResponse(
        List<PrivilegeResponse> privileges,

        boolean hasOrganizerRolesResponse) {
}
