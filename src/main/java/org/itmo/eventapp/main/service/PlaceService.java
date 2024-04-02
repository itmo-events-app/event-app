package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.Place;
import org.itmo.eventapp.main.repository.PlaceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PlaceService {
    private final PlaceRepository placeRepository;

    Place findById(int id) {
        Optional<Place> place = placeRepository.findById(id);
        if (place.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
        }

        return place.get();
    }
}
