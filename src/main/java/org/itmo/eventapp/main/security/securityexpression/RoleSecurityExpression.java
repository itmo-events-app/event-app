package org.itmo.eventapp.main.security.securityexpression;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeName;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Validated
@Service
public class RoleSecurityExpression {
    private final MiscSecurityExpression miscSecurityExpression;

    public boolean canCreateRole() {
        return miscSecurityExpression.checkSystemPrivilege(PrivilegeName.CREATE_ROLE);
    }

    public boolean canEditRole() {
        return miscSecurityExpression.checkSystemPrivilege(PrivilegeName.EDIT_ROLE);
    }

    public boolean canDeleteRole() {
        return miscSecurityExpression.checkSystemPrivilege(PrivilegeName.DELETE_ROLE);
    }

    public boolean canAssignSystemRole() {
        return miscSecurityExpression.checkSystemPrivilege(PrivilegeName.ASSIGN_SYSTEM_ROLE);
    }

    public boolean canRevokeSystemRole() {
        return miscSecurityExpression.checkSystemPrivilege(PrivilegeName.REVOKE_SYSTEM_ROLE);
    }

    public boolean canRevokeOrganizerRole() {
        return miscSecurityExpression.checkSystemPrivilege(PrivilegeName.REVOKE_ORGANIZER_ROLE);
    }

    public boolean canAssignOrganizationalRole(@Positive(message = "Параметр eventId не может быть меньше 1!") int eventId) {
        return miscSecurityExpression.checkEventPrivilege(eventId, PrivilegeName.ASSIGN_ORGANIZATIONAL_ROLE);
    }

    public boolean canRevokeOrganizationalRole(@Positive(message = "Параметр eventId не может быть меньше 1!") int eventId) {
        return miscSecurityExpression.checkEventPrivilege(eventId, PrivilegeName.REVOKE_ORGANIZATIONAL_ROLE);
    }

    public boolean canAssignAssistantRole(@Positive(message = "Параметр eventId не может быть меньше 1!") int eventId) {
        return miscSecurityExpression.checkEventPrivilege(eventId, PrivilegeName.ASSIGN_ASSISTANT_ROLE);
    }

    public boolean canRevokeAssistantRole(@Positive(message = "Параметр eventId не может быть меньше 1!") int eventId) {
        return miscSecurityExpression.checkEventPrivilege(eventId, PrivilegeName.REVOKE_ASSISTANT_ROLE);
    }

    public boolean canAssignOrganizerRole(@Positive(message = "Параметр eventId не может быть меньше 1!") int eventId) {
        return miscSecurityExpression.checkEventPrivilege(eventId, PrivilegeName.ASSIGN_ORGANIZER_ROLE);
    }

    public boolean canGetAllOrganizationalRole(@Positive(message = "Параметр eventId не может быть меньше 1!") int eventId) {
        return canAssignOrganizationalRole(eventId) || canAssignOrganizerRole(eventId) ||
                canAssignAssistantRole(eventId) || canRevokeAssistantRole(eventId) || canRevokeOrganizationalRole(eventId);
    }
}
