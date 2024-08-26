package com.gethealthy.eventservice.controller;

import com.gethealthy.eventservice.model.EventDTO;
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
}
