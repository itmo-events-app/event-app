package org.itmo.eventapp.main.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exception.IncorrectRoleTypeException;
import org.itmo.eventapp.main.exception.NotAllowedException;
import org.itmo.eventapp.main.exception.NotFoundException;
import org.itmo.eventapp.main.model.entity.EventRole;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.enums.RoleType;
import org.itmo.eventapp.main.repository.EventRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventRoleService {
    private final EventRoleRepository eventRoleRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final EventService eventService;

    public void assignOrganizationalRole(Integer userId, Integer roleId, Integer eventId) {
        var user = userService.findById(userId);
        var role = roleService.findRoleById(roleId);
        if (!role.getType().equals(RoleType.EVENT)) throw new IncorrectRoleTypeException("Неверный тип роли");
        var event = eventService.findById(eventId);
        var eventRole = eventRoleRepository.findByUserAndEvent(user, event);
        if (eventRole.isPresent()) {
            if (eventRole.get().getRole().getId().equals(roleService.getOrganizerRole().getId())) {
                var organizersInEvent = eventRoleRepository.findAllByRoleAndEvent(role, event);
                if (organizersInEvent.size() == 1)
                    throw new NotAllowedException("Мероприятия должно содержать не менее одного пользователя с ролью Организатор");
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
        var user = userService.findById(userId);
        var role = roleService.findRoleById(roleId);
        if (!role.getType().equals(RoleType.EVENT)) throw new IncorrectRoleTypeException("Неверный тип роли");
        var event = eventService.findById(eventId);
        if (roleId.equals(roleService.getOrganizerRole().getId())) {
            var eventRole = eventRoleRepository.findAllByRoleAndEvent(role, event);
            if (eventRole.size() == 1)
                throw new NotAllowedException("Мероприятия должно содержать не менее одного пользователя с ролью Организатор");
        }
        var userRoleInEvent = eventRoleRepository.findByUserAndRoleAndEvent(user, role, event);
        userRoleInEvent.ifPresentOrElse(eventRoleRepository::delete,
                () -> {
                    throw new NotFoundException(String.format("У пользователя с id %d нет роли %s в мероприятии с id %d", // TODO: correct the message
                            userId,
                            role.getName(),
                            eventId));
                });
    }

    public List<EventRole> findAllByRole(Role role) {
        return eventRoleRepository.findAllByRole(role);
    }
}
