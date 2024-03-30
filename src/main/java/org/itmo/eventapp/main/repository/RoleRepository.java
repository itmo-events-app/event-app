package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.dto.response.RoleResponse;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<RoleResponse> findAllByType(RoleType type);

    List<RoleResponse> findByNameContainingIgnoreCase(String name);

    Role findByName(String name);
}
