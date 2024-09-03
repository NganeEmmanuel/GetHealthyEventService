package com.gethealthy.eventservice.service;

import com.gethealthy.eventservice.model.Event;
import com.gethealthy.eventservice.model.EventDTO;
import org.springframework.stereotype.Service;

@Service
public class EventMapperService implements MapperService<EventDTO, Event> {

    @Override
    public EventDTO toDTO(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .eventType(event.getEventType())
                .description(event.getDescription())
                .healthStatus(event.getHealthStatus())
                .startDate(event.getStartDate())
                .location(event.getLocation())
                .build();
    }

    @Override
    public Event toEntity(EventDTO eventDTO) {
        return Event.builder()
                .id(eventDTO.getId())
                .recordID(eventDTO.getRecordID())
                .userID(eventDTO.getUserID())
                .title(eventDTO.getTitle())
                .eventType(eventDTO.getEventType())
                .description(eventDTO.getDescription())
                .healthStatus(eventDTO.getHealthStatus())
                .startDate(eventDTO.getStartDate())
                .location(eventDTO.getLocation())
                .build();
    }

    @Override
    public void updateEntity(EventDTO eventDTO, Event event) {
        event.setTitle(eventDTO.getTitle());
        event.setEventType(eventDTO.getEventType());
        event.setDescription(eventDTO.getDescription());
        event.setStartDate(eventDTO.getStartDate());
        event.setHealthStatus(eventDTO.getHealthStatus());
        event.setLocation(eventDTO.getLocation());
    }
}
