package com.gethealthy.eventservice.service;

import com.gethealthy.eventservice.exception.EventNotFoundException;
import com.gethealthy.eventservice.model.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EventService {
    /**
     *Adds an Event to the database records
     *
     * @param eventDTO the object containing the even
     * @param authorizationHeader header for the request
     * @return the DTO object of contain the information added to the database
     */
    ResponseEntity<EventDTO> addEvent(EventDTO eventDTO, String authorizationHeader);

    /**
     *Gets all the Events associated with a particular record
     *
     * @param recordID Long data type
     * @param authorizationHeader header for the request
     * @return a list of eventDTO objects(records) associated with the recordID
     * @throws EventNotFoundException when no event matches the recordID in the database
     */
    ResponseEntity<List<EventDTO>> getEventsByRecordID(Long recordID, String authorizationHeader) throws EventNotFoundException;

    /**
     * Get all Events containing the specific term for a user
     *
     * @param term  the SearchTerm to search the database against
     * @param authorizationHeader header for the request
     * @return a list of event records associated with that user and matches(contains) that term
     * @throws EventNotFoundException when no event matches the search term and also associated to the userID in the database
     */
    ResponseEntity<List<EventDTO>> searchEvents(String term, String authorizationHeader) throws EventNotFoundException;

    /**
     *Gets an event associated with the provided ID
     *
     * @param id long data type identifying the event record in the database
     * @param authorizationHeader header for the request
     * @return the event record in the database
     * @throws EventNotFoundException if no record is found associated with the provided ID
     */
    ResponseEntity<EventDTO> getEvent(Long id, String authorizationHeader) throws EventNotFoundException;

    /**
     *Updates an event in the database if present
     *
     * @param eventDTO the object containing the updated even information
     * @param authorizationHeader header for the request
     * @return the u[dated record from the database
     * @throws EventNotFoundException if no record is found associated with the eventID provided in the DTO object
     */
    ResponseEntity<EventDTO> updateEvent(EventDTO eventDTO, String authorizationHeader) throws EventNotFoundException;

    /**
     * Deletes an event from the database records
     *
     * @param id the eventID
     * @param authorizationHeader header for the request
     * @return response entity with body true if deleted and false if not
     * @throws EventNotFoundException if no event record is found associated with the eventID and userID
     */
    ResponseEntity<Boolean> deleteEvent(Long id, String authorizationHeader) throws EventNotFoundException;

    /**
     * Deletes all events whose id are provided in the list of ids from the database records
     *
     * @param ids the list of eventID
     * @param authorizationHeader header for the request
     * @return response entity with body of true if deleted all and false if not
     */
    ResponseEntity<Boolean> deleteAllEvent(List<Long> ids, String authorizationHeader);

    /**
     * Deletes all event from the database records associated with a illnessRecordID and userID
     *
     * @param recordID the recordID
     * @param authorizationHeader header for the request
     * @return response entity with body true if deleted and false if not
     */
    ResponseEntity<Boolean> deleteAllEventsByRecordID(Long recordID, String authorizationHeader);
}
