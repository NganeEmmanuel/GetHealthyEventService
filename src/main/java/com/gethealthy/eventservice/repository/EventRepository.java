package com.gethealthy.eventservice.repository;

import com.gethealthy.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

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
            "WHERE (to_tsvector('english', title) @@ to_tsquery('english', regexp_replace(:term, '\\s+', ' & ', 'g') || ':*') " +
            "   OR to_tsvector('english', description) @@ to_tsquery('english', regexp_replace(:term, '\\s+', ' & ', 'g') || ':*') " +
            "   OR title ILIKE '%' || :term || '%' " +
            "   OR description ILIKE '%' || :term || '%') " +
            "AND userid = :userID " +
            "ORDER BY start_date", nativeQuery = true)
    Optional<List<Event>> searchEvents(@Param("term") String term, @Param("userID") Long userID);



    Optional<Event> findByIdAndUserID(Long eventId, Long userID);

    void deleteByIdAndUserID(Long eventID, Long userID);

    void deleteAllByRecordIDAndUserID(Long recordID, Long userID);

    Optional<List<Event>> findAllByRecordIDAndUserID(Long recordID, Long userID);
}
