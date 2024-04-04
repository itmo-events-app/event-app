package org.itmo.eventapp.main.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.EventRole;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.enums.RoleType;
import org.itmo.eventapp.main.repository.EventRepository;
import org.itmo.eventapp.main.repository.EventRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventRoleService {
    private final EventRoleRepository eventRoleRepository;
    private final EventRepository eventRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final List<String> notAssignableRoles = Arrays.asList("Администратор", "Читатель", "Организатор");

    public void assignOrganizationalRole(Integer userId, Integer roleId, Integer eventId) {
        var role = roleService.findRoleById(roleId);
        if (notAssignableRoles.contains(role.getName()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нельзя назначить эту роль");

        var user = userService.findById(userId);
        if (!role.getType().equals(RoleType.EVENT))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Неверный тип роли: ожидалась организационная роль");
        var event = eventFindById(eventId);
        var eventRole = eventRoleRepository.findByUserAndEvent(user, event);
        if (eventRole.isPresent()) {
            if (eventRole.get().getRole().getId().equals(roleService.getOrganizerRole().getId())) {
                var organizersInEvent = eventRoleRepository.findAllByRoleAndEvent(roleService.getOrganizerRole(), event);
                if (organizersInEvent.size() == 1)
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Мероприятие должно содержать не менее одного пользователя с ролью Организатор");
            }
            eventRole.get().setRole(role);
            eventRoleRepository.save(eventRole.get());
        } else {
            var newEventRole = EventRole.builder()
                    .user(user)
                    .role(role)
                    .event(event).build();
            eventRoleRepository.save(newEventRole);
        }
    }

    @Transactional
    public void revokeOrganizationalRole(Integer userId, Integer roleId, Integer eventId) {
        var role = roleService.findRoleById(roleId);
        if (notAssignableRoles.contains(role.getName()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нельзя лишить этой роль");

        var user = userService.findById(userId);
        if (!role.getType().equals(RoleType.EVENT))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Неверный тип роли: ожидалась организационная роль");
        var event = eventFindById(eventId);
        if (roleId.equals(roleService.getOrganizerRole().getId())) {
            var eventRole = eventRoleRepository.findAllByRoleAndEvent(role, event);
            if (eventRole.size() == 1)
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Мероприятие должно содержать не менее одного пользователя с ролью Организатор");
        }
        var userRoleInEvent = eventRoleRepository.findByUserAndRoleAndEvent(user, role, event);
        userRoleInEvent.ifPresentOrElse(eventRoleRepository::delete,
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("У пользователя с id %d нет роли %s в мероприятии с id %d",
                            userId,
                            role.getName(),
                            eventId));
                });
    }

    public List<EventRole> findAllByRole(Role role) {
        return eventRoleRepository.findAllByRole(role);
    }

    EventRole save(EventRole eventRole) {
        return eventRoleRepository.save(eventRole);
    }

    //TODO временный фикс, надо переделать
    private Event eventFindById(int id) {
        return eventRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.EVENT_NOT_FOUND_MESSAGE));
    }
}
