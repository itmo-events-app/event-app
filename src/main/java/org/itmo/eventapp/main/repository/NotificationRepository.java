package org.itmo.eventapp.main.repository;

import jakarta.transaction.Transactional;
import org.itmo.eventapp.main.model.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> getAllByUserId(Integer id, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update notification set seen = true where user_id = :id and seen = false;", nativeQuery = true)
    void updateAllSeenByUserId(Integer id);

    void deleteNotificationsBySentTimeBefore(LocalDateTime time);

    Long countByUserId(Integer userId);
}
