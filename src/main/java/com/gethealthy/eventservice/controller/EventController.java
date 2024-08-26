package com.gethealthy.eventservice.controller;

import com.gethealthy.eventservice.model.DeleteRequest;
import com.gethealthy.eventservice.model.EventDTO;
import com.gethealthy.eventservice.model.SearchRequest;
import com.gethealthy.eventservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/add")
    public ResponseEntity<EventDTO> add(@RequestBody EventDTO eventDTO) {
        return ResponseEntity.ok(eventService.addEvent(eventDTO));
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDTO>> getEventsByRecordID(@RequestParam Long recordID) {
        return ResponseEntity.ok(eventService.getEventsByRecordID(recordID));
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventDTO>> searchEvents(@RequestBody SearchRequest searchRequest) {
        return ResponseEntity.ok(eventService.searchEvents(searchRequest));
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<EventDTO> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }

    @PutMapping("update")
    public ResponseEntity<EventDTO> updateEvent(@RequestBody EventDTO eventDTO) {
        return ResponseEntity.ok(eventService.updateEvent(eventDTO));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteEvent(@RequestBody DeleteRequest deleteRequest) {
        return ResponseEntity.ok(eventService.deleteEvent(deleteRequest));
    }
}
