package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepo extends JpaRepository<Place,Integer>{
}
