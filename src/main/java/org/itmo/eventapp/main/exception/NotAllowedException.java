package org.itmo.eventapp.main.exception;

public class NotAllowedException extends RuntimeException {
    public NotAllowedException(String message) {
        super(message);
    }
}
