package org.itmo.eventapp.main.security.securityexpression;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.EventRole;
import org.itmo.eventapp.main.model.entity.Privilege;
import org.itmo.eventapp.main.model.entity.UserLoginInfo;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeName;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class TaskSecurityExpression {
    public boolean canCreateTask(int eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserLoginInfo userLoginInfo = (UserLoginInfo) authentication.getPrincipal();
        Stream<EventRole> eventRoles = userLoginInfo.getUser().getEventRoles().stream()
            .filter(it -> it.getEvent().getId().equals(eventId));

        Stream<Privilege> eventPrivileges = eventRoles
            .map(it -> it.getRole().getPrivileges())
            .flatMap(Collection::stream);

        return eventPrivileges.map(Privilege::getName).anyMatch(it -> it.equals(PrivilegeName.CREATE_TASK));
    }
}
