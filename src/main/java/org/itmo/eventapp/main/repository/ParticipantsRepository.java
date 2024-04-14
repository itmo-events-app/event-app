package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantsRepository extends JpaRepository<Participant, Integer> {
    List<Participant> findAllByEventId(Integer eventId);

    Optional<Participant> findByIdAndEventId(Integer id, Integer eventId);

}
