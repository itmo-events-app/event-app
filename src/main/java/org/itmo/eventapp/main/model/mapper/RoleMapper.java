package org.itmo.eventapp.main.model.mapper;

import org.itmo.eventapp.main.model.dto.request.RoleRequest;
import org.itmo.eventapp.main.model.dto.response.RoleResponse;
import org.itmo.eventapp.main.model.entity.Privilege;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.enums.RoleType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public final class RoleMapper {
    private static final List<String> basicRoles = Arrays.asList("Администратор", "Читатель", "Организатор", "Помощник");

    private RoleMapper() {

    }

    public static RoleResponse roleToRoleResponse(Role role) {
        Boolean isEditable = Boolean.TRUE;
        if (basicRoles.contains(role.getName()))
            isEditable = Boolean.FALSE;
        return new RoleResponse(role.getId(),
                role.getName(),
                role.getDescription(),
                role.getType(),
                PrivilegeMapper.privilegesToPrivilegeResponseList(role.getPrivileges()),
                isEditable);
    }

    public static List<RoleResponse> rolesToRoleResponseList(Collection<Role> roles) {
        return roles.stream()
                .map(RoleMapper::roleToRoleResponse)
                .toList();
    }

    public static Role roleRequestToRole(RoleRequest roleRequest, Stream<Privilege> privileges) {
        return Role.builder()
                .name(roleRequest.name())
                .description(roleRequest.description())
                .type(Boolean.TRUE.equals(roleRequest.isEvent()) ? RoleType.EVENT : RoleType.SYSTEM)
                .privileges(PrivilegeMapper.privilegeStreamToPrivilegeSet(privileges, roleRequest.isEvent())).build();
    }
}
