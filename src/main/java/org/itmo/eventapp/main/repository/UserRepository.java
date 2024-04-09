package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserLoginInfo_Login(String login);

    boolean existsByRole(Role role);
}
