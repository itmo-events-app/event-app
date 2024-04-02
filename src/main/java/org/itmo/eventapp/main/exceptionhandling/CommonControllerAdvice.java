package org.itmo.eventapp.main.exceptionhandling;

import jakarta.validation.ValidationException;
import org.itmo.eventapp.main.exception.IncorrectRoleTypeException;
import org.itmo.eventapp.main.exception.NotAllowedException;
import org.itmo.eventapp.main.exception.NotFoundException;
import org.itmo.eventapp.main.exception.NotUniqueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
class CommonControllerAdvice {
    @ExceptionHandler(ValidationException.class)
    ResponseEntity<String> handleValidationException(ValidationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NotUniqueException.class)
    ResponseEntity<String> handleNotUniqueException(NotUniqueException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(IncorrectRoleTypeException.class)
    ResponseEntity<String> handleNotUniqueException(IncorrectRoleTypeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(NotAllowedException.class)
    ResponseEntity<String> handleNotUniqueException(NotAllowedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ex.getMessage());
    }
}
