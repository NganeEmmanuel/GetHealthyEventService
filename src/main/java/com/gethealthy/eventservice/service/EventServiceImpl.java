package com.gethealthy.eventservice.service;

import com.gethealthy.eventservice.model.Event;
import com.gethealthy.eventservice.model.EventDTO;
import com.gethealthy.eventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final MapperService<EventDTO, Event> mapperService;
    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    @Override
    public EventDTO addEvent(EventDTO eventDTO) {
        try{
            return mapperService.toDTO(eventRepository.save(mapperService.toEntity(eventDTO)));
        }catch(Exception e){
            logger.info("Error while adding event with data: {}", eventDTO);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EventDTO> getEventsByRecordID(Long recordID) {
        return List.of();
    }
}
