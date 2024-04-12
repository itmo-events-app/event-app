package org.itmo.eventapp.main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.itmo.eventapp.main.model.dto.request.LoginRequest;
import org.itmo.eventapp.main.model.dto.request.NewPasswordRequest;
import org.itmo.eventapp.main.model.dto.request.RecoveryPasswordRequest;
import org.itmo.eventapp.main.model.dto.request.RegistrationUserRequest;
import org.itmo.eventapp.main.model.dto.response.RegistrationRequestForAdmin;
import org.itmo.eventapp.main.model.validation.annotation.ValidLogin;
import org.itmo.eventapp.main.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/recoveryPassword")
    ResponseEntity<Void> recoveryPassword(@Valid @RequestBody RecoveryPasswordRequest request) {
        authenticationService.recoverPassword(request.email(), request.returnUrl());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/validateRecoveryToken")
    ResponseEntity<Void> validateRecoveryToken(@RequestParam
                                               @NotBlank(message = "Токен отсутствует") String token) {
        authenticationService.validateRecoveryToken(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/newPassword")
    ResponseEntity<Void> newPassword(@Valid @RequestBody NewPasswordRequest request) {
        authenticationService.newPassword(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
