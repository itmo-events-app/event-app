package org.itmo.eventapp.main.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.EventRole;
import org.itmo.eventapp.main.model.entity.Privilege;
import org.itmo.eventapp.main.model.entity.enums.RoleType;
import org.itmo.eventapp.main.repository.EventRepository;
import org.itmo.eventapp.main.repository.EventRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventRoleService {
    private final EventRoleRepository eventRoleRepository;
    private final EventRepository eventRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final List<String> defaultOrganizationalRoles = Arrays.asList("Помощник", "Организатор");

    public List<EventRole> findByUserIdAndEventId(int userId, int eventId) {
        return eventRoleRepository.findByUserIdAndEventId(userId, eventId);
    }

    public void assignOrganizationalRole(Integer userId, Integer roleId, Integer eventId, boolean isDefaultOrganizationalRole) {
        var role = roleService.findRoleById(roleId);
        if (isDefaultOrganizationalRole) {
            if (!defaultOrganizationalRoles.contains(role.getName()))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionConst.ROLE_ASSIGNMENT_FORBIDDEN_MESSAGE);
        } else {
            if (defaultOrganizationalRoles.contains(role.getName())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionConst.ROLE_ASSIGNMENT_FORBIDDEN_MESSAGE);
            }
            if (!role.getType().equals(RoleType.EVENT))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        ExceptionConst.INVALID_ROLE_TYPE.formatted("организационная"));
        }
        var user = userService.findById(userId);
        var event = eventFindById(eventId);
        if (eventRoleRepository.existsByUserIdAndRoleIdAndEventId(userId, roleId, eventId))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    ExceptionConst.USER_ROLE_ALREADY_EXISTS_IN_EVENT_MESSAGE.formatted(userId, role.getName(), eventId));
        var newEventRole = EventRole.builder()
                .user(user)
                .role(role)
                .event(event).build();
        eventRoleRepository.save(newEventRole);
    }

    @Transactional
    public void revokeOrganizationalRole(Integer userId, Integer roleId, Integer eventId, boolean isDefaultOrganizationalRole) {
        var role = roleService.findRoleById(roleId);
        if (isDefaultOrganizationalRole) {
            if (!defaultOrganizationalRoles.contains(role.getName()))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionConst.ROLE_REVOKING_FORBIDDEN_MESSAGE);
        } else {
            if (defaultOrganizationalRoles.contains(role.getName())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionConst.ROLE_REVOKING_FORBIDDEN_MESSAGE);
            }
            if (!role.getType().equals(RoleType.EVENT))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        ExceptionConst.INVALID_ROLE_TYPE.formatted("организационная"));
        }
        userService.findById(userId);
        eventFindById(eventId);
        if (roleId.equals(roleService.getOrganizerRole().getId())) {
            var eventRole = eventRoleRepository.findAllByRoleIdAndEventId(roleId, eventId);
            if (eventRole.size() == 1)
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionConst.AT_LEAST_ONE_ORGANIZER_MESSAGE);
        }
        var userRoleInEvent = eventRoleRepository.findByUserIdAndRoleIdAndEventId(userId, roleId, eventId);
        userRoleInEvent.ifPresentOrElse(eventRoleRepository::delete,
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            ExceptionConst.USER_ROLE_NOT_FOUND_IN_EVENT_MESSAGE.formatted(userId, role.getName(), eventId));
                });
    }

    public Set<Privilege> getUserEventPrivileges(Integer userId, Integer eventId) {
        EventRole eventRole = eventRoleRepository.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        ExceptionConst.EVENT_ROLE_NOT_FOUND_MESSAGE));
        return eventRole.getRole().getPrivileges();
    }

    EventRole save(EventRole eventRole) {
        return eventRoleRepository.save(eventRole);
    }

    //TODO временный фикс, надо переделать
    private Event eventFindById(int id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        ExceptionConst.EVENT_NOT_FOUND_MESSAGE));
    }

    List<EventRole> findAllByEventId(Integer eventId) {
        return eventRoleRepository.findAllByEventId(eventId);
    }

    @Transactional
    public void saveAll(List<EventRole> eventRoles) {
        eventRoleRepository.saveAll(eventRoles);
    }

    public void deleteByEventId(int eventId) {
        eventRoleRepository.deleteByEventId(eventId);
    }

    EventRole findByUserIdAndEventId(Integer userId, Integer eventId) {
        return eventRoleRepository.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        ExceptionConst.EVENT_NOT_FOUND_MESSAGE));
    }
}
