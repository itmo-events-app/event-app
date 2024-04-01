package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {
    Privilege findPrivilegeById(Integer id);
}
