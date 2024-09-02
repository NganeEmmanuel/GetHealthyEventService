package com.gethealthy.eventservice.service;

import com.gethealthy.eventservice.exception.EventNotFoundException;
import com.gethealthy.eventservice.feign.AuthenticationInterface;
import com.gethealthy.eventservice.model.*;
import com.gethealthy.eventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final MapperService<EventDTO, Event> mapperService;
    private final AuthenticationInterface authenticationInterface;
    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    @Override
    public ResponseEntity<EventDTO> addEvent(EventDTO eventDTO, String authorizationHeader) {
        try{
            eventDTO.setUserID(authenticationInterface.getLoggedInUserId(authorizationHeader).getBody());
            return ResponseEntity.ok(
                    mapperService
                            .toDTO(eventRepository
                                    .save(mapperService
                                            .toEntity(eventDTO)
                                    )
                            )
            );
        }catch(Exception e){
            logger.info("Error while adding event with data: {} and associated with user from header: {}", eventDTO, authorizationHeader);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<List<EventDTO>> getEventsByRecordID(Long recordID, String authorizationHeader) throws EventNotFoundException {
        List<EventDTO> eventDTOList = new ArrayList<>();
        try {
            var userID = authenticationInterface.getLoggedInUserId(authorizationHeader).getBody();
            eventRepository.findAllByRecordIDAndUserID(recordID, userID)
                    .orElseThrow(
                            () -> new EventNotFoundException(recordID)
                    )
                    .forEach(eventDTO -> eventDTOList.add(mapperService.toDTO(eventDTO)));

            return ResponseEntity.ok(eventDTOList);
        }catch (EventNotFoundException eventNotFoundException){
            logger.info("No event found associated with recordID: {}", recordID);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.info("Error while getting events associated with recordID: {}", recordID);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<List<EventDTO>> searchEvents(String term, String authorizationHeader) throws EventNotFoundException {
        List<EventDTO> eventDTOList = new ArrayList<>();
        try{
            var userID = authenticationInterface.getLoggedInUserId(authorizationHeader).getBody();
            eventRepository.searchEvents(term, userID)
                    .orElseThrow(
                            () -> new EventNotFoundException(term, userID)
                    )
                    .forEach(eventDTO -> eventDTOList.add(mapperService.toDTO(eventDTO)));
            return ResponseEntity.ok(eventDTOList);
        }catch (EventNotFoundException eventNotFoundException){
            logger.info("No event found matching term: {} and associated with user from header: {}", term, authorizationHeader);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.info("Error while getting records matching term: {} and associated with user from header: {}", term, authorizationHeader);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<EventDTO> getEvent(Long id, String authorizationHeader) throws EventNotFoundException {
        try {
            var userID = authenticationInterface.getLoggedInUserId(authorizationHeader).getBody();
            return ResponseEntity.ok(
                mapperService
                        .toDTO(eventRepository
                                .findByIdAndUserID(id, userID)
                                .orElseThrow(
                                    () -> new EventNotFoundException(id, userID)
                                )
                        )
            );

        }catch (EventNotFoundException eventNotFoundException){
            logger.info("No event found associated with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch(Exception e){
            logger.info("Error while getting event associated with id: {}", id);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<EventDTO> updateEvent(EventDTO eventDTO, String authorizationHeader) throws EventNotFoundException {
        try {
            var userID = authenticationInterface.getLoggedInUserId(authorizationHeader).getBody();
            if(!eventDTO.getUserID().equals(userID)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            var event = eventRepository.findById(eventDTO.getId()).orElseThrow(
                    () -> new EventNotFoundException(eventDTO.getId())
            );

            mapperService.updateEntity(eventDTO, event);
            return ResponseEntity.ok(mapperService.toDTO(eventRepository.save(event)));
        }catch (EventNotFoundException eventNotFoundException){
            logger.info("No event found associated with id: {} while updating the event", eventDTO.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch(Exception e){
            logger.info("Error while updating event associated with id: {}", eventDTO.getId());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Boolean> deleteEvent(Long id, String authorizationHeader) throws EventNotFoundException {
        try{
            var userID = authenticationInterface.getLoggedInUserId(authorizationHeader).getBody();
            eventRepository.delete(
                    eventRepository.findByIdAndUserID(id, userID)
                            .orElseThrow(
                                    () -> new EventNotFoundException(id, userID)
                            )
            );
            return ResponseEntity.ok(Boolean.TRUE);
        }catch (EventNotFoundException eventNotFoundException){
            logger.info("No event found with id: {} and associated with user from header: {}", id, authorizationHeader);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch(Exception e){
            logger.info("Error while deleting event with id: {} and associated with user from header: {}", id, authorizationHeader);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Boolean> deleteAllEvent(List<Long> ids, String authorizationHeader){
        try{
            var userID = authenticationInterface.getLoggedInUserId(authorizationHeader).getBody();
            ids.forEach(
                    eventID -> eventRepository.deleteByIdAndUserID(eventID, userID)
            );
            return ResponseEntity.ok(Boolean.TRUE);
        }catch(Exception e){
            logger.info("Error while deleting events associated with one of the ids: {} and associated with user from header: {}", ids, authorizationHeader);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Boolean> deleteAllEventsByRecordID(Long recordID, String authorizationHeader){
        try{
            var userID = authenticationInterface.getLoggedInUserId(authorizationHeader).getBody();
            eventRepository.deleteAllByRecordIDAndUserID(recordID, userID);
            return ResponseEntity.ok(Boolean.TRUE);
        }catch(Exception e){
            logger.info("Error while deleting events associated with recordID: {} and associated with user from header: {}", recordID, authorizationHeader);
            throw new RuntimeException(e);
        }
    }
}
