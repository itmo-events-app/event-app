package org.itmo.eventapp.main.model.mapper;

import org.itmo.eventapp.main.model.dto.response.UserRoleResponse;
import org.itmo.eventapp.main.model.entity.EventRole;

import java.util.List;

public class EventRoleMapper {
    private EventRoleMapper() {
    }

    public static UserRoleResponse eventRoleToUserRoleResponse(EventRole eventRole) {
        return new UserRoleResponse(
                eventRole.getUser().getId(),
                eventRole.getUser().getName(),
                eventRole.getUser().getSurname(),
                eventRole.getRole().getName());
    }

    public static List<UserRoleResponse> eventRolesToUserRoleResponses(List<EventRole> eventRoles) {
        return eventRoles.stream()
                .map(EventRoleMapper::eventRoleToUserRoleResponse)
                .toList();
    }

}
