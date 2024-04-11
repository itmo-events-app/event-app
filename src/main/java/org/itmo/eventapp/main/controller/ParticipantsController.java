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
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/events/{id}/participants")
@Validated
public class ParticipantsController {
    private final ParticipantsService participantsService;

    @GetMapping
    public ResponseEntity<List<ParticipantResponse>> getParticipants(@Min(1) @PathVariable("id") Integer id) {
        List<Participant> participants = participantsService.getParticipants(id);
        return ResponseEntity.ok().body(ParticipantMapper.participantsToResponseList(participants));
    }

    @PutMapping
    public ResponseEntity<ParticipantResponse> changePresence(@PathVariable("id") Integer id, @RequestBody ParticipantPresenceRequest participantPresenceRequest) {
        Participant participant = participantsService.changePresence(id, participantPresenceRequest);
        return ResponseEntity.ok().body(ParticipantMapper.participantToResponse(participant));
    }

    @PostMapping
    public ResponseEntity<List<ParticipantResponse>> setPartisipantsList(@PathVariable("id") Integer id, @RequestBody ParticipantsListRequest participantsListRequest) throws IOException {
        List<Participant> participants = participantsService.setParticipants(id, participantsListRequest);
        return ResponseEntity.ok().body(ParticipantMapper.participantsToResponseList(participants));
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> getParticipantsXlsxFile(@PathVariable("id") Integer id) throws IOException {
        String path = participantsService.getParticipantsXlsx(id);
        Path filePath = Paths.get(path);
        Resource fileResource = new FileSystemResource(filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filePath.getFileName().toString() + "\"");
        headers.set("Content-Type","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(filePath.toFile().length())
                .body(fileResource);
    }

}
