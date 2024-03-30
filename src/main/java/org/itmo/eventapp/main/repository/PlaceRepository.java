package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place,Integer>{
}
