package org.itmo.eventapp.main.security.securityexpression;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.EventRole;
import org.itmo.eventapp.main.model.entity.Privilege;
import org.itmo.eventapp.main.model.entity.UserLoginInfo;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeName;
import org.itmo.eventapp.main.service.EventRoleService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class TaskSecurityExpression {
    private final EventRoleService eventRoleService;

    public boolean canCreateTask(int eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserLoginInfo userLoginInfo = (UserLoginInfo) authentication.getPrincipal();

        List<EventRole> eventRoles = eventRoleService.findByUserIdAndEventId(userLoginInfo.getUser().getId(), eventId);

        Stream<Privilege> eventPrivileges = eventRoles.stream()
            .map(it -> it.getRole().getPrivileges())
            .flatMap(Collection::stream);

        return eventPrivileges.map(Privilege::getName).anyMatch(it -> it.equals(PrivilegeName.CREATE_TASK));

    }
}
