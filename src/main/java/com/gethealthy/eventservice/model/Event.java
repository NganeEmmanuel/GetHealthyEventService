package com.gethealthy.eventservice.model;

import com.gethealthy.eventservice.enums.EventType;
import com.gethealthy.eventservice.enums.HealthStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long recordID;
    private Long userID;
    private String title;
    @Column(nullable = false)
    private EventType eventType;
    private String description;
    private String location;
    private HealthStatus healthStatus;
    private String startDate;
    private Date addedDate;

    @PrePersist
    protected void onCreate() {
        addedDate = new Date();
    }
}
