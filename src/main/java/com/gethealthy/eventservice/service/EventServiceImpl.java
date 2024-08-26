package com.gethealthy.eventservice.service;

import com.gethealthy.eventservice.exception.EventNotFoundException;
import com.gethealthy.eventservice.model.Event;
import com.gethealthy.eventservice.model.EventDTO;
import com.gethealthy.eventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<EventDTO> eventDTOList = new ArrayList<>();
        try {
            eventRepository.findAllByRecordID(recordID)
                    .orElseThrow(
                            () -> new EventNotFoundException(recordID)
                    )
                    .forEach(eventDTO -> eventDTOList.add(mapperService.toDTO(eventDTO)));

            return eventDTOList;
        }catch (EventNotFoundException eventNotFoundException){
            logger.info("No event found associated with recordID: {}", recordID);
            throw new RuntimeException(eventNotFoundException);
        }catch(Exception e){
            logger.info("Error while getting events associated with recordID: {}", recordID);
            throw new RuntimeException(e);
        }
    }
}
