package org.itmo.eventapp.main.repository;


import org.itmo.eventapp.main.model.entity.TaskObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskObjectRepository extends JpaRepository<TaskObject, Integer> {
}
