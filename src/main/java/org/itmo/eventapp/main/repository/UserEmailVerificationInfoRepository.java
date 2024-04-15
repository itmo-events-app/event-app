package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.UserEmailVerificationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEmailVerificationInfoRepository extends JpaRepository<UserEmailVerificationInfo, Integer> {

    Optional<UserEmailVerificationInfo> findByToken(String token);
}
