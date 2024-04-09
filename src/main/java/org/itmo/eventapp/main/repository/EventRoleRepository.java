package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.EventRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRoleRepository extends JpaRepository<EventRole, Integer> {

    boolean existsByRoleId(Integer roleId);

    List<EventRole> findByUserIdAndEventId(int userId, int eventId);

    Optional<EventRole> findByUserIdAndEventId(Integer userId, Integer eventId);

    List<EventRole> findAllByRoleIdAndEventId(Integer roleId, Integer eventId);

    Optional<EventRole> findByUserIdAndRoleIdAndEventId(Integer userId, Integer roleId, Integer eventId);

    List<EventRole> findAllByEventId(Integer eventId);

    boolean existsByUserIdAndRoleIdAndEventId(Integer userId, Integer roleId, Integer eventId);

    long deleteByEventId(int eventId);
}
