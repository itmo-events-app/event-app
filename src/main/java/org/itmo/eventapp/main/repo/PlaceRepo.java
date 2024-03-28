package org.itmo.eventapp.main.repo;

import org.itmo.eventapp.main.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepo extends JpaRepository<Place,Integer>{
}
