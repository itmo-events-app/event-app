package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantsRepository extends JpaRepository<Participant, Integer> {
    List<Participant> findAllByEvent(Event event);
    Participant findByIdAndEvent(Integer id, Event event);

}
