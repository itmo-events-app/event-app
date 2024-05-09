package org.itmo.eventapp.main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.dto.request.PlaceRequest;
import org.itmo.eventapp.main.model.entity.Place;
import org.itmo.eventapp.main.model.mapper.PlaceMapper;
import org.itmo.eventapp.main.repository.PlaceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PlaceService {
    private final PlaceRepository placeRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Place> getAllOrFilteredPlaces(int page, int size, String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Place> query = cb.createQuery(Place.class);
        Root<Place> root = query.from(Place.class);
        List<Predicate> predicates = new ArrayList<>();
        if (name != null) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        query.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Place> typedQuery = entityManager.createQuery(query);

        typedQuery.setFirstResult(page * size);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }

    public Place findById(int id) {
        return placeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.PLACE_NOT_FOUND_MESSAGE));
    }

    public Place edit(Integer id, PlaceRequest placeRequest) {
        Place place = findById(id);
        Place newPlace = PlaceMapper.placeRequestToPlace(placeRequest);
        newPlace.setId(place.getId());
        return save(newPlace);
    }

    public void delete(Integer id) {
        placeRepository.deleteById(id);
    }

    public Place save(Place place) {
        return placeRepository.save(place);
    }
}
