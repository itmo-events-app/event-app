package org.itmo.eventapp.main.model.mapper;

import org.itmo.eventapp.main.model.dto.request.RoleRequest;
import org.itmo.eventapp.main.model.dto.response.RoleResponse;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.enums.RoleType;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public final class RoleMapper {

    private RoleMapper() {

    }

    public static RoleResponse roleToRoleResponse(Role role) {
        return new RoleResponse(role.getId(),
                role.getName(),
                role.getDescription(),
                role.getType(),
                PrivilegeMapper.privilegesToPrivilegeResponseList(role.getPrivileges()));
    }

    public static List<RoleResponse> rolesToRoleResponseList(Collection<Role> roles) {
        return roles.stream()
                .map(RoleMapper::roleToRoleResponse)
                .toList();
    }

    public static Role roleRequestToRole(RoleRequest roleRequest) {
        return Role.builder()
                .name(roleRequest.name())
                .description(roleRequest.description())
                .type(Boolean.TRUE.equals(roleRequest.isEvent()) ? RoleType.EVENT : RoleType.SYSTEM)
                .privileges(new HashSet<>()).build();
    }
}
