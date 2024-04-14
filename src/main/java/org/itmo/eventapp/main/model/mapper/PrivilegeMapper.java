package org.itmo.eventapp.main.model.mapper;

import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.dto.response.PrivilegeResponse;
import org.itmo.eventapp.main.model.entity.Privilege;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeType;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

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

    public static Set<Privilege> privilegeStreamToPrivilegeSet(Stream<Privilege> privileges, Boolean isEvent) {
        var privilegeType = Boolean.TRUE.equals(isEvent) ? PrivilegeType.EVENT : PrivilegeType.SYSTEM;
        Set<Privilege> privilegesSet = new HashSet<>();
        privileges.forEach(privilege -> {
            if (checkInvalidPrivilegeType(privilege, privilegeType)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionConst.INVALID_PRIVILEGE_TYPE);
            }
            privilegesSet.add(privilege);
        });
        return privilegesSet;
    }

    private static boolean checkInvalidPrivilegeType(Privilege privilege, PrivilegeType type) {
        return !privilege.getType().equals(type);
    }
}
