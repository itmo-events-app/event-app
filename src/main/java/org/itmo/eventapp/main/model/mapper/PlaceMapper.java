package org.itmo.eventapp.main.model.mapper;

import org.itmo.eventapp.main.model.dto.response.PlaceShortDataResponse;
import org.itmo.eventapp.main.model.entity.Place;

public class PlaceMapper {

    private PlaceMapper(){

    }

    public static PlaceShortDataResponse placeToPlaceShortResponse(Place place) {
        if (place == null) {
            return null;
        }
        return new PlaceShortDataResponse(
                place.getId(),
                place.getName(),
                place.getAddress()
        );
    }
}
