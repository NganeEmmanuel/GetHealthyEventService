package com.gethealthy.eventservice.service;

import com.gethealthy.eventservice.exception.EventNotFoundException;
import com.gethealthy.eventservice.model.*;
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
    public List<EventDTO> getEventsByRecordID(Long recordID) throws EventNotFoundException {
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

    @Override
    public List<EventDTO> searchEvents(SearchRequest searchRequest) throws EventNotFoundException {
        List<EventDTO> eventDTOList = new ArrayList<>();
        try{
            eventRepository.searchEvents(searchRequest.getTerm(), searchRequest.getUserID())
                    .orElseThrow(
                            () -> new EventNotFoundException(searchRequest.getTerm(), searchRequest.getUserID())
                    )
                    .forEach(eventDTO -> eventDTOList.add(mapperService.toDTO(eventDTO)));
            return eventDTOList;
        }catch (EventNotFoundException eventNotFoundException){
            logger.info("No event found associated with term: {} and matching the userID: {}", searchRequest.getTerm(), searchRequest.getUserID());
            throw new RuntimeException(eventNotFoundException);
        }catch(Exception e){
            logger.info("Error while getting records associated with term: {} and userID: {}", searchRequest.getTerm(), searchRequest.getUserID());
            throw new RuntimeException(e);
        }
    }

    @Override
    public EventDTO getEvent(Long id) throws EventNotFoundException {
        try {
        return mapperService.toDTO(eventRepository.findById(id).orElseThrow(
                () -> new EventNotFoundException(id)
        ));

        }catch (EventNotFoundException eventNotFoundException){
            logger.info("No event found associated with id: {}", id);
            throw new RuntimeException(eventNotFoundException);
        }catch(Exception e){
            logger.info("Error while getting event associated with id: {}", id);
            throw new RuntimeException(e);
        }
    }

    @Override
    public EventDTO updateEvent(EventDTO eventDTO) throws EventNotFoundException {
        try {
            var event = eventRepository.findById(eventDTO.getId()).orElseThrow(
                    () -> new EventNotFoundException(eventDTO.getId())
            );

            mapperService.updateEntity(eventDTO, event);
            return mapperService.toDTO(eventRepository.save(event));
        }catch (EventNotFoundException eventNotFoundException){
            logger.info("No event found associated with id: {} while updating the event", eventDTO.getId());
            throw new RuntimeException(eventNotFoundException);
        }catch(Exception e){
            logger.info("Error while updating event associated with id: {}", eventDTO.getId());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean deleteEvent(DeleteRequest deleteRequest) throws EventNotFoundException {
        try{
            eventRepository.delete(
                    eventRepository.findByIdAndUserID(deleteRequest.getEventID(), deleteRequest.getUserID())
                            .orElseThrow(
                                    () -> new EventNotFoundException(deleteRequest.getEventID())
                            )
            );
            return Boolean.TRUE;
        }catch (EventNotFoundException eventNotFoundException){
            logger.info("No event found associated with id: {} and userID: {}", deleteRequest.getEventID(), deleteRequest.getUserID());
            throw new RuntimeException(eventNotFoundException);
        }catch(Exception e){
            logger.info("Error while deleting event associated with id: {} and userID: {}", deleteRequest.getEventID(), deleteRequest.getUserID());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean deleteAllEvent(EventsDeleteRequest eventsDeleteRequest){
        try{
            eventsDeleteRequest.getEventIDList().forEach(
                    eventID -> eventRepository.deleteByIdAndUserID(eventID, eventsDeleteRequest.getUserID())
            );
            return Boolean.TRUE;
        }catch(Exception e){
            logger.info("Error while deleting events associated with ids: {} and userID: {}", eventsDeleteRequest.getEventIDList(), eventsDeleteRequest.getUserID());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean deleteAllEventsByRecordID(RecordEventsDeleteRequest deleteRequest){
        try{
            eventRepository.deleteAllByRecordIDAndUserID(deleteRequest.getRecordID(), deleteRequest.getUserID());
            return Boolean.TRUE;
        }catch(Exception e){
            logger.info("Error while deleting events associated with recordID: {} and userID: {}", deleteRequest.getRecordID(), deleteRequest.getUserID());
            throw new RuntimeException(e);
        }
    }
}
