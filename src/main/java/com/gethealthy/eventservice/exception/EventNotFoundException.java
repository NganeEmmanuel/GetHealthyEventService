package com.gethealthy.eventservice.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long id) {
        super("Event not found with id " + id);
    }

    public EventNotFoundException(String message) {
        super(message);
    }
}
