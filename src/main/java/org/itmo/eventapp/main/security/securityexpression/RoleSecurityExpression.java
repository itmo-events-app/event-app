package org.itmo.eventapp.main.security.securityexpression;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.EventRole;
import org.itmo.eventapp.main.model.entity.Privilege;
import org.itmo.eventapp.main.model.entity.UserLoginInfo;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeName;
import org.itmo.eventapp.main.service.EventRoleService;
import org.itmo.eventapp.main.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Validated
@Service
public class RoleSecurityExpression {
    private final UserService userService;
    private final EventRoleService eventRoleService;

    private int getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserLoginInfo userLoginInfo = (UserLoginInfo) authentication.getPrincipal();
        return userLoginInfo.getUser().getId();
    }

    private Stream<Privilege> getUserSystemPrivileges(int userId) {
        var user = userService.findById(userId);
        return user.getRole().getPrivileges().stream();
    }

    private boolean checkSystemPrivilege(PrivilegeName name) {
        var privileges = getUserSystemPrivileges(getCurrentUserId());
        return privileges.map(Privilege::getName).anyMatch(it -> it.equals(name));
    }

    private Stream<Privilege> getUserEventPrivileges(int eventId, int userId) {
        List<EventRole> eventRoles = eventRoleService.findByUserIdAndEventId(userId, eventId);
        return eventRoles.stream()
                .map(it -> it.getRole().getPrivileges())
                .flatMap(Collection::stream);
    }

    private boolean checkEventPrivilege(int eventId, PrivilegeName privilegeName) {
        Stream<Privilege> eventPrivileges = getUserEventPrivileges(eventId, getCurrentUserId());
        return eventPrivileges.map(Privilege::getName).anyMatch(it -> it.equals(privilegeName));
    }

    public boolean canCreateRole() {
        return checkSystemPrivilege(PrivilegeName.CREATE_ROLE);
    }

    public boolean canEditRole() {
        return checkSystemPrivilege(PrivilegeName.EDIT_ROLE);
    }

    public boolean canDeleteRole() {
        return checkSystemPrivilege(PrivilegeName.DELETE_ROLE);
    }

    public boolean canAssignSystemRole() {
        return checkSystemPrivilege(PrivilegeName.ASSIGN_SYSTEM_ROLE);
    }

    public boolean canRevokeSystemRole() {
        return checkSystemPrivilege(PrivilegeName.REVOKE_SYSTEM_ROLE);
    }

    public boolean canRevokeOrganizerRole() {
        return checkSystemPrivilege(PrivilegeName.REVOKE_ORGANIZER_ROLE);
    }

    public boolean canAssignOrganizationalRole(@Positive(message = "Параметр eventId не может быть меньше 1!") int eventId) {
        return checkEventPrivilege(eventId, PrivilegeName.ASSIGN_ORGANIZATIONAL_ROLE);
    }

    public boolean canRevokeOrganizationalRole(@Positive(message = "Параметр eventId не может быть меньше 1!") int eventId) {
        return checkEventPrivilege(eventId, PrivilegeName.REVOKE_ORGANIZATIONAL_ROLE);
    }

    public boolean canAssignAssistantRole(@Positive(message = "Параметр eventId не может быть меньше 1!") int eventId) {
        return checkEventPrivilege(eventId, PrivilegeName.ASSIGN_ASSISTANT_ROLE);
    }

    public boolean canRevokeAssistantRole(@Positive(message = "Параметр eventId не может быть меньше 1!") int eventId) {
        return checkEventPrivilege(eventId, PrivilegeName.REVOKE_ASSISTANT_ROLE);
    }

    public boolean canAssignOrganizerRole(@Positive(message = "Параметр eventId не может быть меньше 1!") int eventId) {
        return checkEventPrivilege(eventId, PrivilegeName.ASSIGN_ORGANIZER_ROLE) || checkSystemPrivilege(PrivilegeName.ASSIGN_ORGANIZER_ROLE);
    }

    public boolean canGetAllOrganizationalRole(@Positive(message = "Параметр eventId не может быть меньше 1!") int eventId) {
        return canAssignOrganizationalRole(eventId) || canAssignOrganizerRole(eventId) ||
                canAssignAssistantRole(eventId) || canRevokeAssistantRole(eventId) || canRevokeOrganizationalRole(eventId);
    }
}
