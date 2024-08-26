package com.gethealthy.eventservice.repository;

import com.gethealthy.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<List<Event>> findAllByRecordID(Long recordID);

    /**
     * query for postgresql that retrieves all records from the Event(model name) table
     * WHERE the tile column contains or is the closest match to the term (prioritize best match)
     * OR  description column contains or is the closest match to the term (prioritize best match)
     * AND the userID is the same as the userID parameter
     * ORDER BY dateAdded
     *
     * @param term string to search for or match against
     * @param userID Long data type for user identifier
     * @return a list of the best possible matched records if available
     */
    @Query(value = "SELECT * FROM event " +
            "WHERE (to_tsvector('english', title) @@ plainto_tsquery('english', :term) " +
            "   OR to_tsvector('english', description) @@ plainto_tsquery('english', :term)) " +
            "AND userid = :userID " +
            "ORDER BY start_date", nativeQuery = true)
    Optional<List<Event>> searchEvents(@Param("term") String term, @Param("userID") Long userID);


    void findByIdAndUserID(Long eventId, Long userID);
}
