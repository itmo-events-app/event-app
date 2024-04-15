package org.itmo.eventapp.main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.LoginRequest;
import org.itmo.eventapp.main.model.dto.request.RegistrationUserRequest;
import org.itmo.eventapp.main.model.dto.response.RegistrationRequestForAdmin;
import org.itmo.eventapp.main.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Получение логина пользователя")
    @PostMapping("/login")
    ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authenticationService.login(loginRequest);
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/register")
    ResponseEntity<Void> register(@Valid @RequestBody RegistrationUserRequest registrationUserRequest) {
        authenticationService.createRegisterRequest(registrationUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Одобрение заявки на регистрацию")
    @PostMapping(value = "/approveRegister/{requestId}")
    ResponseEntity<Void> approveRegister(@PathVariable("requestId") @Parameter(name = "requestId", description = "ID заявки на регистрацию", example = "1") Integer requestId) {
        // TODO: check for administrator
        authenticationService.approveRegistrationRequestCallback(requestId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "/declineRegister/{requestId}")
    ResponseEntity<Void> declineRegister(@PathVariable("requestId") Integer requestId) {
        // TODO: check for administrator
        authenticationService.declineRegistrationRequestCallback(requestId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/listRegisterRequests")
    ResponseEntity<List<RegistrationRequestForAdmin>> listRegisterRequests() {
        // TODO: check for administrator
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(authenticationService.listRegisterRequestsCallback());
    }

    @PostMapping("/sendVerificationEmail")
    ResponseEntity<Void> sendVerificationEmail(@RequestParam String returnUrl) {
        authenticationService.sendVerificationEmail(returnUrl);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/validateEmailVerificationToken")
    ResponseEntity<Void> validateEmailVerificationToken(@RequestParam String token) {
        authenticationService.validateEmailVerificationToken(token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/verifyEmail")
    ResponseEntity<Void> verifyEmail() {
        authenticationService.verifyEmail();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
