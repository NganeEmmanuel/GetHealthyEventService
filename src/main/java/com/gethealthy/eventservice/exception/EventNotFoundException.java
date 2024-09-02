package com.gethealthy.eventservice.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long id) {
        super("Event not found with id " + id);
    }

    public EventNotFoundException(String message) {
        super(message);
    }

    public EventNotFoundException(String message, Long id) {
        super("No event found matching the term: {" + message + "} and also associated with userID: {" + id +"}");
    }

    public EventNotFoundException(Long id, Long userID){
        super("No event found with id: {" + id + "} and associated with userID: {" + userID + "}");
    }
}
