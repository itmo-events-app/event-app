package org.itmo.eventapp.main.model.dto.response;

import org.itmo.eventapp.main.model.entity.enums.PrivilegeName;

public record PrivilegeResponse(Integer id,
                                PrivilegeName name,
                                String description) {
}
