package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.LoginAttempts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginAttemptsRepository extends JpaRepository<LoginAttempts, Integer> {

    Optional<LoginAttempts> findByUserLoginInfo_Login(String login);
}
