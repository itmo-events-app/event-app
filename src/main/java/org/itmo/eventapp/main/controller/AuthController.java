package org.itmo.eventapp.main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.LoginRequest;
import org.itmo.eventapp.main.model.dto.request.NewPasswordRequest;
import org.itmo.eventapp.main.model.dto.request.RecoveryPasswordRequest;
import org.itmo.eventapp.main.model.dto.request.RegistrationUserRequest;
import org.itmo.eventapp.main.model.dto.response.RegistrationRequestForAdmin;
import org.itmo.eventapp.main.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@Validated
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

    @Operation(summary = "Утверждение заявки на регистрацию в системе")
    @PreAuthorize("@authSecurityExpression.canApproveRegistrationRequest()")
    @PostMapping(value = "/approveRegister/{requestId}")
    ResponseEntity<Void> approveRegister(
        @Positive(message = "Параметр requestId не может быть меньше 1!") @PathVariable("requestId") @Parameter(name = "requestId", description = "ID заявки на регистрацию", example = "1") Integer requestId) {
        authenticationService.approveRegistrationRequestCallback(requestId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Отклонение заявки на регистрацию в системе")
    @PostMapping(value = "/declineRegister/{requestId}")
    @PreAuthorize("@authSecurityExpression.canRejectRegistrationRequest()")
    ResponseEntity<Void> declineRegister(
        @Positive(message = "Параметр requestId не может быть меньше 1!") @PathVariable("requestId") @Parameter(name = "requestId", description = "ID заявки на регистрацию", example = "1") Integer requestId) {
        authenticationService.declineRegistrationRequestCallback(requestId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Получение списка всех заявок на регистрацию в системе")
    @GetMapping(value = "/listRegisterRequests")
    @PreAuthorize("@authSecurityExpression.canApproveRegistrationRequest() or @authSecurityExpression.canRejectRegistrationRequest()")
    ResponseEntity<List<RegistrationRequestForAdmin>> listRegisterRequests() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(authenticationService.listRegisterRequestsCallback());
    }

    @Operation(summary = "Отправка запроса на восстановление пароля")
    @PostMapping("/recoveryPassword")
    ResponseEntity<Void> recoveryPassword(@Valid @RequestBody RecoveryPasswordRequest request) {
        authenticationService.recoverPassword(request.email(), request.returnUrl());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Запрос на валидацию токена")
    @PostMapping("/validateRecoveryToken")
    ResponseEntity<Void> validateRecoveryToken(@NotBlank(message = "Токен отсутствует")
                                               @RequestParam
                                               @Parameter(name = "token", description = "Токен восстановления пароля", example = "c5b7bcc0-cffe-4f57-853c-7fa18e56b36d")
                                               String token) {
        authenticationService.validateRecoveryToken(token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Запрос на смену пароля")
    @PostMapping("/newPassword")
    ResponseEntity<Void> newPassword(@Valid @RequestBody NewPasswordRequest request) {
        authenticationService.newPassword(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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
