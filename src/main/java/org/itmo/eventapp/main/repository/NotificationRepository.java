package org.itmo.eventapp.main.repository;

import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import org.itmo.eventapp.main.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> getAllByUser_Id(Integer id);

    @Modifying
    @Transactional
    @Query(value = "update notification set seen = true, read_time = now() where user_id = :id and seen = false;", nativeQuery = true)
    void updateAllSeenByUser_Id(Integer id);
}
