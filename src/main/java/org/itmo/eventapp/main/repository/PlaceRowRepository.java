package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.PlaceRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRowRepository extends JpaRepository<PlaceRow, Integer> {
}
