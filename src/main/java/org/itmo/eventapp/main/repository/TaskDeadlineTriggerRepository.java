package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.model.entity.TaskDeadlineTrigger;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskDeadlineTriggerRepository extends ListCrudRepository<TaskDeadlineTrigger, Integer> {
    @Query("SELECT tdt.task FROM TaskDeadlineTrigger tdt WHERE tdt.triggerTime < :triggerTime")
    List<Task> findTasksByTriggerTimeBefore(LocalDateTime triggerTime);

    void deleteAllByTriggerTimeBefore(LocalDateTime triggerTime);
}
