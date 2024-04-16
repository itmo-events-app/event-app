package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.EventRole;
import org.itmo.eventapp.main.model.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRoleRepository extends JpaRepository<EventRole, Integer> {

    boolean existsByRoleId(Integer roleId);

    List<EventRole> findByUserIdAndEventId(int userId, int eventId);

    boolean existsByUserId(int userId);

    List<EventRole> findAllByRoleIdAndEventId(Integer roleId, Integer eventId);

    Optional<EventRole> findByUserIdAndRoleIdAndEventId(Integer userId, Integer roleId, Integer eventId);

    List<EventRole> findAllByEventId(Integer eventId);

    boolean existsByUserIdAndRoleIdAndEventId(Integer userId, Integer roleId, Integer eventId);

    long deleteByEventId(int eventId);

    List<EventRole> findAllByUserIdAndRoleId(Integer userId, Integer roleId);

    @Query("SELECT er FROM EventRole er WHERE er.user.id = :userId and :privilege MEMBER OF er.role.privileges")
    List<EventRole> findAllByUserIdAndRolePrivilegesIsContaining(Integer userId, @Param("privilege") Privilege privilege);

    List<EventRole> findAllByUserId(Integer userId);
}
