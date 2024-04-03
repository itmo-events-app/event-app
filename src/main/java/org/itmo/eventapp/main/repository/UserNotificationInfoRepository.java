package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.UserNotificationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNotificationInfoRepository extends JpaRepository<UserNotificationInfo, Integer> {
}
