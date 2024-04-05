package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Integer> {
    List<Event> findAllByParentId(Integer parentId);
}
