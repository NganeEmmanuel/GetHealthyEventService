package com.gethealthy.eventservice.repository;

import com.gethealthy.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {

    Optional<List<Event>> findAllByRecordID(Long recordID);
}
