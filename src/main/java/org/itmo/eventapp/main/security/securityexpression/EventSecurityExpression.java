package org.itmo.eventapp.main.security.securityexpression;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.EventRole;
import org.itmo.eventapp.main.model.entity.Privilege;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeName;
import org.itmo.eventapp.main.service.EventRoleService;
import org.itmo.eventapp.main.service.EventService;
import org.itmo.eventapp.main.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Validated
@Service
public class EventSecurityExpression {
    private final MiscSecurityExpression miscSecurityExpression;

    private final EventRoleService eventRoleService;

    private final EventService eventService;

    public boolean canCreateEvent() {
        return miscSecurityExpression.checkSystemPrivilege(PrivilegeName.CREATE_EVENT);
    }

    public boolean canCreateActivity(@Min(value = 1, message = "Параметр eventId не может быть меньше 1!") int eventId) {
        eventId = miscSecurityExpression.getParentEventOrSelfId(eventId);
        return miscSecurityExpression.checkEventPrivilege(eventId, PrivilegeName.CREATE_EVENT_ACTIVITIES);
    }

    public boolean canUpdateEvent(@Min(value = 1, message = "Параметр eventId не может быть меньше 1!") int eventId) {
        int eventOrParentId = miscSecurityExpression.getParentEventOrSelfId(eventId);
        if (eventId == eventOrParentId) {
            return miscSecurityExpression.checkEventPrivilege(eventOrParentId, PrivilegeName.EDIT_EVENT_INFO);
        }
        return miscSecurityExpression.checkEventPrivilege(eventOrParentId, PrivilegeName.EDIT_EVENT_ACTIVITIES);
    }

    public boolean canDeleteEventOrActivity(@Min(value = 1, message = "Параметр eventId не может быть меньше 1!") int eventId) {
        int eventOrParentId = miscSecurityExpression.getParentEventOrSelfId(eventId);
        if (eventId == eventOrParentId) {
            return miscSecurityExpression.checkSystemPrivilege(PrivilegeName.CREATE_EVENT);
        }
        return miscSecurityExpression.checkEventPrivilege(eventOrParentId, PrivilegeName.DELETE_EVENT_ACTIVITIES);
    }

    public boolean canGetEvents() {
        return miscSecurityExpression.checkSystemPrivileges(
                Arrays.asList(
                        PrivilegeName.SEARCH_EVENTS_AND_ACTIVITIES,
                        PrivilegeName.VIEW_ALL_EVENTS));
    }

    public boolean canGetUsersHavingRoles(@Min(value = 1, message = "Параметр eventId не может быть меньше 1!") int eventId) {
        eventId = miscSecurityExpression.getParentEventOrSelfId(eventId);
        return miscSecurityExpression.checkEventPrivilege(eventId, PrivilegeName.VIEW_ORGANIZER_USERS);
    }
}
