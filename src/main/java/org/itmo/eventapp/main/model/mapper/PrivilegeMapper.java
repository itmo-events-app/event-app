package org.itmo.eventapp.main.model.mapper;

import org.itmo.eventapp.main.model.dto.response.PrivilegeResponse;
import org.itmo.eventapp.main.model.entity.Privilege;

import java.util.Collection;
import java.util.List;

public final class PrivilegeMapper {

    private PrivilegeMapper() {

    }

    public static PrivilegeResponse privilegeToPrivilegeResponse(Privilege privilege) {
        return new PrivilegeResponse(privilege.getId(),
                privilege.getName(),
                privilege.getDescription());
    }

    public static List<PrivilegeResponse> privilegesToPrivilegeResponseList(Collection<Privilege> privileges) {
        return privileges.stream()
                .map(PrivilegeMapper::privilegeToPrivilegeResponse)
                .toList();
    }
}
