package com.weatherstation.backend.entity;

import com.weatherstation.backend.enums.RainIntensity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class RainEventTimeline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private RainIntensity intensity;

    public RainEventTimeline() {
    }

    public Long getId() {
        return id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public RainIntensity getIntensity() {
        return intensity;
    }

    public void setIntensity(RainIntensity intensity) {
        this.intensity = intensity;
    }
}
