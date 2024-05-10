package org.itmo.eventapp.main.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.EventRole;
import org.itmo.eventapp.main.model.entity.Privilege;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeName;
import org.itmo.eventapp.main.model.entity.enums.RoleType;
import org.itmo.eventapp.main.model.mapper.EventMapper;
import org.itmo.eventapp.main.repository.EventRepository;
import org.itmo.eventapp.main.repository.EventRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

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
        List<EventRole> eventRoles = eventRoleRepository.findByUserIdAndEventId(userId, eventId);
        if (eventRoles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.EVENT_ROLE_NOT_FOUND_MESSAGE);
        }
        Set<Privilege> privileges = new HashSet<>();
        eventRoles.stream()
            .map(it -> it.getRole().getPrivileges())
            .forEach(privileges::addAll);
        return privileges;
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

    public Map<String, List<String>> findAllUserEventRolesGroupedByEvent(Integer userId) {
        List<EventRole> userRoles = eventRoleRepository.findAllByUserId(userId);
        Map<String, List<String>> rolesByEvent = new HashMap<>();
        for (EventRole eventRole : userRoles) {
            rolesByEvent.computeIfAbsent(eventRole.getEvent().getTitle() + " (id:" + eventRole.getEvent().getId() + ")", k -> new ArrayList<>())
                    .add(eventRole.getRole().getName());
        }
        return rolesByEvent;
    }

    @Transactional
    public void saveAll(List<EventRole> eventRoles) {
        eventRoleRepository.saveAll(eventRoles);
    }

    public void deleteByEventId(int eventId) {
        eventRoleRepository.deleteByEventId(eventId);
    }

    public List<Event> getEventsByRole(Integer userId, Integer roleId) {
        return EventMapper.eventRolesToEvents(eventRoleRepository.findAllByUserIdAndRoleId(userId, roleId));
    }

    public List<Event> getEventsByPrivilege(Integer userId, Integer privilegeId) {
        Privilege privilege = new Privilege();
        privilege.setId(privilegeId);
        return EventMapper.eventRolesToEvents(eventRoleRepository
                .findAllByUserIdAndRolePrivilegesIsContaining(userId, privilege));
    }

    public boolean userHasOrganizerRoles(Integer userId) {
        return eventRoleRepository.existsByUserId(userId);
    }
}
