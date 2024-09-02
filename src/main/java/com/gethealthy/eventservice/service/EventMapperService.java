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
                .recordID(event.getRecordID())
                .userID(event.getUserID())
                .title(event.getTitle())
                .description(event.getDescription())
                .startDate(event.getStartDate())
                .build();
    }

    @Override
    public Event toEntity(EventDTO eventDTO) {
        return Event.builder()
                .id(eventDTO.getId())
                .recordID(eventDTO.getRecordID())
                .userID(eventDTO.getUserID())
                .title(eventDTO.getTitle())
                .description(eventDTO.getDescription())
                .startDate(eventDTO.getStartDate())
                .build();
    }

    @Override
    public void updateEntity(EventDTO eventDTO, Event event) {
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setStartDate(eventDTO.getStartDate());
    }
}
