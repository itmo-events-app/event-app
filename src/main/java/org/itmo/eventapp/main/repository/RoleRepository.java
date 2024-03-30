package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.dto.RoleDto;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<RoleDto> findAllByType(RoleType type);

//    List<RoleDto> findAll();
}
