package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.ParticipantsListRequest;
import org.itmo.eventapp.main.model.entity.Participant;
import org.itmo.eventapp.main.repository.ParticipantsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ParticipantsService {
    private final ParticipantsRepository participantsRepository;
    private final EventService eventService;
    public List<Participant> getParticipants(Integer id) {
        return participantsRepository.findAllByEvent(eventService.findById(id));
    }

    //ToDo Method
    public Participant changePresence(Integer eventId, Integer participantId){
        Participant participant = new Participant();
        return participant;
    }

    //ToDo Method
    public List<Participant> setParticipants(ParticipantsListRequest participantsListRequest){
        List<Participant> participants = new ArrayList<>();
        return participants;
    }
}
