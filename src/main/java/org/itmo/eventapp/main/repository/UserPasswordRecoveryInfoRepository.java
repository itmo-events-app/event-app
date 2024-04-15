package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.UserPasswordRecoveryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPasswordRecoveryInfoRepository extends JpaRepository<UserPasswordRecoveryInfo, Integer> {

    Optional<UserPasswordRecoveryInfo> findByToken(String token);
}
