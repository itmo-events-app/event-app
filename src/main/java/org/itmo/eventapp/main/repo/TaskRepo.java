package org.itmo.eventapp.main.repo;

import org.itmo.eventapp.main.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task,Integer>{
}
