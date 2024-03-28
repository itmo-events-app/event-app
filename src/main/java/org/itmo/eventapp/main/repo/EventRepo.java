package org.itmo.eventapp.main.repo;

import org.itmo.eventapp.main.model.Event;
import org.itmo.eventapp.main.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepo extends JpaRepository<Event,Integer> {
}
