package com.gethealthy.eventservice.controller;

import com.gethealthy.eventservice.model.*;
import com.gethealthy.eventservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("event/add")
    public ResponseEntity<EventDTO> add(@RequestBody EventDTO eventDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return eventService.addEvent(eventDTO, authorizationHeader);
    }

    @GetMapping("/record/get-all")
    public ResponseEntity<List<EventDTO>> getEventsByRecordID(@RequestParam Long recordID, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return eventService.getEventsByRecordID(recordID, authorizationHeader);
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventDTO>> searchEvents(@RequestParam String term, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return eventService.searchEvents(term, authorizationHeader);
    }

    @GetMapping("/event/get-with-id")
    public ResponseEntity<EventDTO> getEvent(@RequestParam Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return eventService.getEvent(id, authorizationHeader);
    }

    @PutMapping("event/update")
    public ResponseEntity<EventDTO> updateEvent(@RequestBody EventDTO eventDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return eventService.updateEvent(eventDTO, authorizationHeader);
    }

    @DeleteMapping("event/delete")
    public ResponseEntity<Boolean> deleteEvent(@RequestParam Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return eventService.deleteEvent(id, authorizationHeader);
    }

    @DeleteMapping("delete/all-with-ids")
    public ResponseEntity<Boolean> deleteAllEvents(@RequestParam List<Long> ids, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return eventService.deleteAllEvent(ids, authorizationHeader);
    }

    @DeleteMapping("record/delete-all")
    public ResponseEntity<Boolean> deleteAllEventsByRecordID(@RequestParam Long recordID, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return eventService.deleteAllEventsByRecordID(recordID, authorizationHeader);
    }
}
