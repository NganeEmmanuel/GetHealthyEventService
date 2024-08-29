package com.gethealthy.eventservice.service;

import com.gethealthy.eventservice.exception.EventNotFoundException;
import com.gethealthy.eventservice.model.*;

import java.util.List;

public interface EventService {
    /**
     *Adds an Event to the database records
     *
     * @param eventDTO the object containing the even information
     * @return the DTO object of contain the information added to the database
     */
    EventDTO addEvent(EventDTO eventDTO);

    /**
     *Gets all the Events associated with a particular record
     *
     * @param recordID Long data type
     * @return a list of eventDTO objects(records) associated with the recordID
     * @throws EventNotFoundException when no event matches the recordID in the database
     */
    List<EventDTO> getEventsByRecordID(Long recordID) throws EventNotFoundException;

    /**
     * Get all Events containing the specific term for a user
     *
     * @param searchRequest Object containing the SearchTerm and userID
     * @return a list of event records associated with that user and matches(contains) that term
     * @throws EventNotFoundException when no event matches the search term and also associated to the userID in the database
     */
    List<EventDTO> searchEvents(SearchRequest searchRequest) throws EventNotFoundException;

    /**
     *Gets an event associated with the provided ID
     *
     * @param id long data type identifying the event record in the database
     * @return the event record in the database
     * @throws EventNotFoundException if no record is found associated with the provided ID
     */
    EventDTO getEvent(Long id) throws EventNotFoundException;

    /**
     *
     * @param eventDTO the object containing the updated even information
     * @return the u[dated record from the database
     * @throws EventNotFoundException if no record is found associated with the eventID provided in the DTO object
     */
    EventDTO updateEvent(EventDTO eventDTO) throws EventNotFoundException;

    /**
     * Deletes an event from the database records
     *
     * @param deleteRequest the object containing the eventID and the userID
     * @return true if deleted and false if not
     * @throws EventNotFoundException if no event record is found associated with the eventID and userID
     */
    Boolean deleteEvent(DeleteRequest deleteRequest) throws EventNotFoundException;

    /**
     * Deletes all events whose id are provided in the deleteRequest from the database records
     *
     * @param eventsDeleteRequest the object containing the list of eventID and the userID
     * @return true if deleted all and false if not
     */
    Boolean deleteAllEvent(EventsDeleteRequest eventsDeleteRequest);

    /**
     * Deletes all event from the database records associated with a illnessRecordID and userID
     *
     * @param deleteRequest the object containing the recordID and the userID
     * @return true if deleted and false if not
     */
    Boolean deleteAllEventsByRecordID(RecordEventsDeleteRequest deleteRequest);
}
