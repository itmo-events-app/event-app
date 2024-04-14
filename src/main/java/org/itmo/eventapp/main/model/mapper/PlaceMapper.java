package org.itmo.eventapp.main.model.mapper;

import org.itmo.eventapp.main.model.dto.request.PlaceRequest;
import org.itmo.eventapp.main.model.dto.response.PlaceResponse;
import org.itmo.eventapp.main.model.dto.response.PlaceShortDataResponse;
import org.itmo.eventapp.main.model.entity.Place;

import java.util.List;

public class PlaceMapper {

    private PlaceMapper() {
    }

    public static PlaceResponse placeToPlaceResponse(Place place) {
        return new PlaceResponse(
            place.getId(),
            place.getName(),
            place.getAddress(),
            place.getFormat(),
            place.getRoom(),
            place.getDescription(),
            place.getLatitude(),
            place.getLongitude(),
            place.getRenderInfo()
        );
    }

    public static Place placeRequestToPlace(PlaceRequest placeRequest) {
        return Place.builder()
            .name(placeRequest.name())
            .address(placeRequest.address())
            .format(placeRequest.format())
            .room(placeRequest.room())
            .description(placeRequest.description())
            .latitude(placeRequest.latitude())
            .longitude(placeRequest.longitude())
            .build();
    }


    public static List<PlaceResponse> placesToPlaceResponseList(List<Place> places) {
        return places.stream()
            .map(PlaceMapper::placeToPlaceResponse)
            .toList();
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
