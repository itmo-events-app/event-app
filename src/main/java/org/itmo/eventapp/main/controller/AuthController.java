package org.itmo.eventapp.main.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.itmo.eventapp.main.model.dto.request.LoginRequest;
import org.itmo.eventapp.main.model.dto.request.RegistrationUserRequest;
import org.itmo.eventapp.main.model.entity.RegistrationRequest;
import org.itmo.eventapp.main.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authenticationService.login(loginRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    ResponseEntity<Void> register(@Valid @RequestBody RegistrationUserRequest registrationUserRequest) {
        authenticationService.createRegisterRequest(registrationUserRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "/approveRegister/{requestId}")
    ResponseEntity<Void> approveRegister(@PathVariable("requestId") Integer requestId) {
        authenticationService.approveRegistrationRequestCallback(requestId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "/declineRegister/{requestId}")
    ResponseEntity<Void> declineRegister(@PathVariable("requestId") Integer requestId) {
        authenticationService.declineRegistrationRequestCallback(requestId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/listRegisterRequests")
    ResponseEntity<List<RegistrationRequest>> listRegisterRequests() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticationService.listRegisterRequestsCallback());
    }
}
