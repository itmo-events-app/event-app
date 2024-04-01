package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.EventRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRoleRepository extends JpaRepository<EventRole,Integer> {
}
