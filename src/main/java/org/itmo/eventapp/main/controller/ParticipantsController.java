package org.itmo.eventapp.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.CreateEventRequest;
import org.itmo.eventapp.main.model.dto.request.ParticipantPresenceRequest;
import org.itmo.eventapp.main.model.dto.request.ParticipantsListRequest;
import org.itmo.eventapp.main.model.dto.response.ParticipantResponse;
import org.itmo.eventapp.main.model.entity.Participant;
import org.itmo.eventapp.main.model.mapper.ParticipantMapper;
import org.itmo.eventapp.main.service.ParticipantsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/events/")
@Validated
public class ParticipantsController {
    private final ParticipantsService participantsService;

    @GetMapping("/{id}/participants/list")
    public ResponseEntity<List<ParticipantResponse>> getParticipants(@Min(1) @PathVariable("id") Integer id) {
        List<Participant> participants = participantsService.getParticipants(id);
        return ResponseEntity.ok().body(ParticipantMapper.participantsToResponseList(participants));
    }

    @PutMapping("/{id}/participants")
    public ResponseEntity<ParticipantResponse> changePresence(@PathVariable("id") Integer id, @RequestBody ParticipantPresenceRequest participantPresenceRequest) {
        Participant participant = participantsService.changePresence(id, participantPresenceRequest);
        return ResponseEntity.ok().body(ParticipantMapper.participantToResponse(participant));
    }

    @PostMapping("/{id}/participants/newlist")
    public ResponseEntity<List<ParticipantResponse>> setPartisipantsList(@PathVariable("id") Integer id /*, @RequestBody ParticipantsListRequest participantsListRequest*/) throws IOException {
        List<Participant> participants = participantsService.setParticipants(id);
        return ResponseEntity.ok().body(ParticipantMapper.participantsToResponseList(participants));
    }

    @GetMapping("/{id}/participants/list/xlsx")
    public ResponseEntity<List<ParticipantResponse>> getParticipantsXlsxFile( @PathVariable("id") Integer id) throws IOException {
        List<Participant> participants = participantsService.getParticipantsXlsx(id);
        return ResponseEntity.ok().body(ParticipantMapper.participantsToResponseList(participants));
    }
}
