package org.itmo.eventapp.main.security.securityexpression;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeName;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Validated
@Service
public class AuthSecurityExpression {
    private final MiscSecurityExpression miscSecurityExpression;

    public boolean canApproveRegistrationRequest() {
        return miscSecurityExpression.checkSystemPrivilege(PrivilegeName.APPROVE_REGISTRATION_REQUEST);
    }

    public boolean canRejectRegistrationRequest() {
        return miscSecurityExpression.checkSystemPrivilege(PrivilegeName.REJECT_REGISTRATION_REQUEST);
    }
}
