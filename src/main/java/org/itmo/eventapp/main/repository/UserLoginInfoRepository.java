package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.UserLoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoginInfoRepository extends JpaRepository<UserLoginInfo, Integer> {

    Optional<UserLoginInfo> getUserLoginInfoByEmail(String email);
}
