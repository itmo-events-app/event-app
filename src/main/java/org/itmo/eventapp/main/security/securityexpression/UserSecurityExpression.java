package org.itmo.eventapp.main.security.securityexpression;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeName;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Validated
@Service
public class UserSecurityExpression {
    private final MiscSecurityExpression miscSecurityExpression;

    public boolean canGetAllUsers() {
        return miscSecurityExpression.checkSystemPrivilege(PrivilegeName.VIEW_OTHER_USERS_PROFILE);
    }
}
