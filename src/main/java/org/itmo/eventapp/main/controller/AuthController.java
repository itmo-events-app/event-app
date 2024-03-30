package org.itmo.eventapp.main.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.LoginRequest;
import org.itmo.eventapp.main.model.dto.RegistrationUserRequest;
import org.itmo.eventapp.main.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authenticationService.login(loginRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationUserRequest registrationUserRequest) {
        authenticationService.createRegisterRequest(registrationUserRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/approveRegister")
    public ResponseEntity<Void> approveRegister(@RequestBody int requestId) {
        authenticationService.approveRegistrationRequestCallback(requestId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
