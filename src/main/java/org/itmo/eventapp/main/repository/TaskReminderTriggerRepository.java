package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.model.entity.TaskReminderTrigger;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskReminderTriggerRepository extends ListCrudRepository<TaskReminderTrigger, Integer> {
    @Query("SELECT tdt.task FROM TaskReminderTrigger tdt WHERE tdt.triggerTime < :beforeTime")
    List<Task> findTasksByTriggerTimeBefore(LocalDateTime beforeTime);

    void deleteAllByTriggerTimeBefore(LocalDateTime triggerTime);
}
