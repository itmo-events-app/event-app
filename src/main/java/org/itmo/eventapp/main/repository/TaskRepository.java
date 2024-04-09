package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.EventRole;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {
    List<Task> findAllByEventId(Integer eventId);
    List<Task> findAllByReminderBetween(LocalDateTime startTime, LocalDateTime endTime);
    List<Task> findAllByDeadlineBetween(LocalDateTime startTime, LocalDateTime endTime);
}