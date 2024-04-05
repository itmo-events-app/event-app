package org.itmo.eventapp.main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.minio.MinioService;
import org.itmo.eventapp.main.model.dto.request.CreateEventRequest;
import org.itmo.eventapp.main.model.dto.response.UserRoleResponse;
import org.itmo.eventapp.main.model.entity.*;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.Place;
import org.itmo.eventapp.main.model.entity.enums.EventFormat;
import org.itmo.eventapp.main.model.entity.enums.EventStatus;
import org.itmo.eventapp.main.model.mapper.EventMapper;
import org.itmo.eventapp.main.model.mapper.EventRoleMapper;
import org.itmo.eventapp.main.repository.EventRepository;
import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;

    private final MinioService minioService;
    private final String bucketName="event-images";

    private final PlaceService placeService;
    private final UserService userService;
    private final RoleService roleService;
    private final EventRoleService eventRoleService;
    private final TaskService taskService;

    @PersistenceContext
    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Event addEvent(EventRequest eventRequest) {
        Place place = placeService.findById(eventRequest.placeId());

        Event parent = findById(eventRequest.parent());
        if (parent.getParent() != null) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, ExceptionConst.ACTIVITY_RECURSION);
        }

        Event e = Event.builder()
                .place(place)
                .startDate(eventRequest.startDate())
                .endDate(eventRequest.endDate())
                .title(eventRequest.title())
                .shortDescription(eventRequest.shortDescription())
                .fullDescription(eventRequest.fullDescription())
                .format(eventRequest.format())
                .status(eventRequest.status())
                .registrationStart(eventRequest.registrationStart())
                .registrationEnd(eventRequest.registrationEnd())
                .parent(parent)
                .participantLimit(eventRequest.participantLimit())
                .participantAgeLowest(eventRequest.participantAgeLowest())
                .participantAgeHighest(eventRequest.participantAgeHighest())
                .preparingStart(eventRequest.preparingStart())
                .preparingEnd(eventRequest.preparingEnd())
                .build();
        eventRepository.save(e);
        MultipartFile image = eventRequest.image();
        if(!Objects.isNull(image)) {
            String modifiedImageName = e.getId().toString() + "." + FilenameUtils.getExtension(image.getOriginalFilename());
            minioService.uploadWithModifiedFileName(image, bucketName, modifiedImageName);
        }
        return e;
    }

    @Transactional
    public Event addEventByOrganizer(CreateEventRequest eventRequest) {
        User user = userService.findById(eventRequest.userId());
        Event e = Event.builder()
                .title(eventRequest.title())
                .build();
        Event savedEvent = eventRepository.save(e);

        // TODO: Do not get organizer from DB each time.
        Role role = roleService.getOrganizerRole();

        EventRole eventRole = EventRole.builder()
                .user(user)
                .role(role)
                .event(savedEvent)
                .build();
        eventRoleService.save(eventRole);
        return savedEvent;
    }

    public Event findById(int id) {
        return eventRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.EVENT_NOT_FOUND_MESSAGE));
    }

    public Event updateEvent(Integer id, EventRequest eventRequest) {
        if (!eventRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.EVENT_NOT_FOUND_MESSAGE);
        }
        Place place = placeService.findById(eventRequest.placeId());
        Event parentEvent = null;
        if (eventRequest.parent() != null) {
            parentEvent = eventRepository.findById(eventRequest.parent())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.EVENT_PARENT_NOT_FOUND_MESSAGE));
        }
        Event updatedEvent = EventMapper.eventRequestToEvent(id, eventRequest, place, parentEvent);
        eventRepository.save(updatedEvent);
        MultipartFile image = eventRequest.image();
        minioService.deleteImageByEvent(bucketName, updatedEvent.getId().toString());
        if (!Objects.isNull(image)) {
            String modifiedImageName = updatedEvent.getId().toString() + "." + FilenameUtils.getExtension(image.getOriginalFilename());
            minioService.uploadWithModifiedFileName(image, bucketName, modifiedImageName);
        }
        return updatedEvent;
    }

    @SuppressWarnings("java:S107")
    public List<Event> getAllOrFilteredEvents(int page, int size, Integer parentId, String title,
                                                      LocalDateTime startDate, LocalDateTime endDate,
                                                      EventStatus status, EventFormat format) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        // If parentId is null, we don't want the activities to return
        if (parentId == null) {
            predicates.add(cb.isNull(root.get("parent")));
        } else {
            predicates.add(cb.equal(root.get("parent").get("id"), parentId));
        }

        if (title != null) {
            predicates.add(cb.equal(root.get("title"), title));
        }
        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), startDate));
        }
        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), endDate));
        }
        if (startDate != null && endDate != null) {
            predicates.add(cb.between(root.get("startDate"), startDate, endDate));
        }
        if (status != null) {
            predicates.add(cb.equal(root.get("status"), status));
        }
        if (format != null) {
            predicates.add(cb.equal(root.get("format"), format));
        }

        query.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Event> typedQuery = entityManager.createQuery(query);

        typedQuery.setFirstResult(page * size);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }

    public Event getEventById(Integer id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.EVENT_NOT_FOUND_MESSAGE));
    }

    public void deleteEventById(Integer id) {
        eventRepository.deleteById(id);
    }

    /*TODO: TEST*/
    public boolean checkOneEvent(Event first, Event second) {
        boolean firstParent = (second.getParent() != null) &&
                (Objects.equals(second.getParent().getId(), first.getId()));
        boolean firstChild = (second.getParent() == null) &&
                (first.getParent() != null) &&
                (Objects.equals(second.getId(), first.getParent().getId()));
        boolean bothChildren = (second.getParent() != null) &&
                (first.getParent() != null) &&
                (Objects.equals(second.getParent().getId(), first.getParent().getId()));

        return firstParent || firstChild || bothChildren;
    }
    public List<EventRole> getUsersHavingRoles(Integer id) {
        return eventRoleService.findAllByEventId(id);
    }

    public Event copyEvent(int id, boolean deep) {
        Event existingEvent = findById(id);
        Event copiedEvent = Event.builder()
                .place(existingEvent.getPlace())
                .startDate(existingEvent.getStartDate())
                .endDate(existingEvent.getEndDate())
                .title(existingEvent.getTitle())
                .shortDescription(existingEvent.getShortDescription())
                .fullDescription(existingEvent.getFullDescription())
                .format(existingEvent.getFormat())
                .status(existingEvent.getStatus())
                .registrationStart(existingEvent.getRegistrationStart())
                .registrationEnd(existingEvent.getRegistrationEnd())
                .parent(existingEvent)
                .participantLimit(existingEvent.getParticipantLimit())
                .participantAgeLowest(existingEvent.getParticipantAgeLowest())
                .participantAgeHighest(existingEvent.getParticipantAgeHighest())
                .preparingStart(existingEvent.getPreparingStart())
                .preparingEnd(existingEvent.getPreparingEnd())
                .build();
        Event savedEvent = eventRepository.save(copiedEvent);

        if (deep) {
            List<EventRole> eventRoles = eventRoleService.findAllByEventId(existingEvent.getId());
            for (EventRole eventRole : eventRoles) {
                EventRole copiedEventRole = EventRole.builder()
                        .event(savedEvent)
                        .user(eventRole.getUser())
                        .role(eventRole.getRole())
                        .build();
                eventRoleService.save(copiedEventRole);
            }
            List<Integer> taskIds = taskService.findAllByEventId(existingEvent.getId()).stream().map(Task::getId).collect(Collectors.toList());
            taskService.copyTasks(savedEvent.getId(),taskIds);
        }
        return savedEvent;
    }
}
