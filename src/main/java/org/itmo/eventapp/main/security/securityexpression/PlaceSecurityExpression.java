package org.itmo.eventapp.main.security.securityexpression;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeName;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Validated
@Service
public class PlaceSecurityExpression {
    private final MiscSecurityExpression miscSecurityExpression;

    public boolean canCreatePlace() {
        return miscSecurityExpression.checkSystemPrivilege(PrivilegeName.CREATE_EVENT_VENUE);
    }

    public boolean canEditPlace() {
        return miscSecurityExpression.checkSystemPrivilege(PrivilegeName.EDIT_EVENT_VENUE);
    }

    public boolean canDeletePlace() {
        return miscSecurityExpression.checkSystemPrivilege(PrivilegeName.DELETE_EVENT_VENUE);
    }
}
