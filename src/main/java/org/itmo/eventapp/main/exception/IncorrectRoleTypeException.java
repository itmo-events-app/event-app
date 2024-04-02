package org.itmo.eventapp.main.exception;

public class IncorrectRoleTypeException extends RuntimeException {
    public IncorrectRoleTypeException(String message) {
        super(message);
    }
}
