package org.itmo.eventapp.main.exception;

public class NotUniqueException extends RuntimeException {
    public NotUniqueException(String message) {
        super(message);
    }
}
