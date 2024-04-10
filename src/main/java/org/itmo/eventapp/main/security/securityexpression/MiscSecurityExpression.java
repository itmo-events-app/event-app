package org.itmo.eventapp.main.security.securityexpression;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.*;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeName;
import org.itmo.eventapp.main.service.EventRoleService;
import org.itmo.eventapp.main.service.EventService;
import org.itmo.eventapp.main.service.UserLoginInfoService;
import org.itmo.eventapp.main.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Validated
@Service
public class MiscSecurityExpression {
    private final EventRoleService eventRoleService;

    private final EventService eventService;

    private final UserService userService;

    private final UserLoginInfoService userLoginInfoService;

    public int getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserLoginInfo userLoginInfo = userLoginInfoService.findByLogin(authentication.getName());
        return userLoginInfo.getUser().getId();
    }

    public int getParentEventOrSelfId(int eventId) {
        Event event = eventService.getEventById(eventId);
        return (event.getParent() == null) ? event.getId() : event.getParent().getId();
    }

    public Stream<Privilege> getUserEventPrivileges(int eventId, int userId) {

        List<EventRole> eventRoles = eventRoleService.findByUserIdAndEventId(userId, eventId);

        return eventRoles.stream()
                .map(it -> it.getRole().getPrivileges())
                .flatMap(Collection::stream);
    }

    public boolean checkEventPrivilege(int eventId, PrivilegeName privilegeName) {
        Stream<Privilege> eventPrivileges = getUserEventPrivileges(eventId, getCurrentUserId());
        return eventPrivileges.map(Privilege::getName).anyMatch(it -> it.equals(privilegeName));
    }

    public Set<Privilege> getUserSystemPrivileges(int userId) {
        // TODO: Should we check for null here?
        return userService.findById(userId).getRole().getPrivileges();
    }

    public boolean checkSystemPrivilege(PrivilegeName privilegeName) {
        return getUserSystemPrivileges(getCurrentUserId())
                .stream()
                .map(Privilege::getName)
                .anyMatch(it -> it.equals(privilegeName));
    }

    public boolean checkSystemPrivileges(List<PrivilegeName> privilegeNames) {
        Set<PrivilegeName> names = getUserSystemPrivileges(getCurrentUserId())
                .stream()
                .map(Privilege::getName)
                .collect(Collectors.toSet());
        for (PrivilegeName privilegeName : privilegeNames) {
            if (!names.contains(privilegeName)) {
                return false;
            }
        }
        return true;
    }

}
