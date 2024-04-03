package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.EventRole;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRoleRepository extends JpaRepository<EventRole, Integer> {

    List<EventRole> findAllByRole(Role role);

    Optional<EventRole> findByUserAndEvent(User user, Event event);

    List<EventRole> findAllByRoleAndEvent(Role role, Event event);

    Optional<EventRole> findByUserAndRoleAndEvent(User user, Role role, Event event);

    List<EventRole> findAllByUserId(Integer userId);

    List<EventRole> findAllByEventId(Integer eventId);
}
