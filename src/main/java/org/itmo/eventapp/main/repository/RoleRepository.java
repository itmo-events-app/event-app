package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.dto.response.RoleResponse;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<RoleResponse> findAllByType(RoleType type);

    List<RoleResponse> findByNameContainingIgnoreCase(String name);

    Optional<Role> findByName(String name);
}
