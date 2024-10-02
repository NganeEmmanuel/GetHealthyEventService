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
import java.util.Optional;

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
            //get user from authorizationHeader by making call to authentication service vai feign client and set dto user
            eventDTO.setUserID(authenticationInterface.getLoggedInUserId(authorizationHeader).getBody());

            //add event to database and return the added event dto
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
            //get userID from authorizationHeader by making call to authentication service vai feign client
            var userID = authenticationInterface.getLoggedInUserId(authorizationHeader).getBody();
            //search database for the events associated with the recordID and userID
            eventRepository.findAllByRecordIDAndUserID(recordID, userID)
                    .orElseThrow(
                            () -> new EventNotFoundException(recordID)
                    )
                    //add results to the eventDto list
                    .forEach(eventDTO -> eventDTOList.add(mapperService.toDTO(eventDTO)));

            return ResponseEntity.ok(eventDTOList);
        }catch (EventNotFoundException eventNotFoundException){
            //This catch block is not needed since we are handling this exception with the GlobalExceptionHandler
            //This applies to similar catch blocks already handled by the Global exception handler class (Across all services). So I have removed them.
            //I am leaving this here for understanding purpose only. So you know an alternative way of handling exceptions
            logger.info("No event found associated with recordID: {}", recordID);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(Exception e){
            //this catch block on the other hand is very well needed to throw the runtime exception which is handled by the GlobalExceptionHandler
            //this block handles unplanned exceptions that occur during processing. So I let them as it should be
            logger.info("Error while getting events associated with recordID: {}", recordID);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<List<EventDTO>> searchEvents(String term, String authorizationHeader) throws EventNotFoundException {
        List<EventDTO> eventDTOList = new ArrayList<>();
        try{
            //get userID from authorizationHeader by making call to authentication service vai feign client
            var userID = authenticationInterface.getLoggedInUserId(authorizationHeader).getBody();
            //search database for records containing and matching the term
            eventRepository.searchEvents(term, userID)
                    .orElseThrow(
                            () -> new EventNotFoundException(term, userID)
                    )
                    //add results to eventDto list
                    .forEach(eventDTO -> eventDTOList.add(mapperService.toDTO(eventDTO)));
            return ResponseEntity.ok(eventDTOList);
        }catch(Exception e){
            logger.info("Error while getting records matching term: {} and associated with user from header: {}", term, authorizationHeader);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<EventDTO> getEvent(Long id, String authorizationHeader) throws EventNotFoundException {
        try {
            //get userID from authorizationHeader by making call to authentication service vai feign client
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
        }catch(Exception e){
            logger.info("Error while getting event associated with id: {}", id);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<EventDTO> updateEvent(EventDTO eventDTO, String authorizationHeader) throws EventNotFoundException {
        try {
            //get userID from authorizationHeader by making call to authentication service vai feign client interface
            var userID = authenticationInterface.getLoggedInUserId(authorizationHeader).getBody();

            // Note user ID from DTO during update is set in the client from the user information in the cache
            // this information in the cache is set when the user logs in(a request is sent to the auth service to get logged-in user)
            if(!eventDTO.getUserID().equals(userID)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            var event = eventRepository.findById(eventDTO.getId()).orElseThrow(
                    () -> new EventNotFoundException(eventDTO.getId())
            );

            mapperService.updateEntity(eventDTO, event);
            return ResponseEntity.ok(mapperService.toDTO(eventRepository.save(event)));
        }catch(Exception e){
            logger.info("Error while updating event associated with id: {}", eventDTO.getId());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Boolean> deleteEvent(Long id, String authorizationHeader) throws EventNotFoundException {
        try{
            //get userID from authorizationHeader by making call to authentication service vai feign client interface
            var userID = authenticationInterface.getLoggedInUserId(authorizationHeader).getBody();
            eventRepository.delete(
                    eventRepository.findByIdAndUserID(id, userID)
                            .orElseThrow(
                                    () -> new EventNotFoundException(id, userID)
                            )
            );
            return ResponseEntity.ok(Boolean.TRUE);
        }catch(Exception e){
            logger.info("Error while deleting event with id: {} and associated with user from header: {}", id, authorizationHeader);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Boolean> deleteAllEvent(List<Long> ids, String authorizationHeader){
       List<Long> notFoundIds = new ArrayList<>();
        try{
            //get userID from authorizationHeader by making call to authentication service vai feign client interface
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
            //get userID from authorizationHeader by making call to authentication service vai feign client
            var userID = authenticationInterface.getLoggedInUserId(authorizationHeader).getBody();
            //check if there are events associated with recordID and userID
            eventRepository.findAllByRecordIDAndUserID(recordID, userID).orElseThrow(
                    () -> new EventNotFoundException("No event found associated with recordID:" + recordID + ", and userID: " + userID)
            );
            //delete associated events
            eventRepository.deleteAllByRecordIDAndUserID(recordID, userID);
            return ResponseEntity.ok(Boolean.TRUE);
        }catch(Exception e){
            logger.info("Error while deleting events associated with recordID: {} and associated with user from header: {}", recordID, authorizationHeader);
            throw new RuntimeException(e);
        }
    }
}
