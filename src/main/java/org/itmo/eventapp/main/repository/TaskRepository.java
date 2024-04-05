package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.Task;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends ListCrudRepository<Task, Integer> {
    List<Task> findAllByNotificationDeadlineBetween(LocalDateTime startTime, LocalDateTime endTime);
    List<Task> findAllByDeadlineBetween(LocalDateTime startTime, LocalDateTime endTime);
}
