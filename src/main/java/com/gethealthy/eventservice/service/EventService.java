package com.gethealthy.eventservice.service;

import com.gethealthy.eventservice.model.EventDTO;

import java.util.List;

public interface EventService {
    /**
     *
     * @param eventDTO the object containing the even information
     * @return the DTO object of contain the information added to the database
     */
    EventDTO addEvent(EventDTO eventDTO);

    /**
     *
     * @param recordID Long data type
     * @return a list of eventDTO objects(records) associated with the recordID
     */
    List<EventDTO> getEventsByRecordID(Long recordID);
}
