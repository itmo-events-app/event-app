package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.EventRole;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.Task;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends ListCrudRepository<Task, Integer> {
    List<Task> findAllByEventId(Integer eventId);
}
