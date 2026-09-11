package com.weatherstation.backend.entity;

import com.weatherstation.backend.enums.RainEventStatus;
import com.weatherstation.backend.enums.RainIntensity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rain_event")
public class RainEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "durationSeconds")
    private Long durationSeconds;

    @Column(name = "intensity")
    @Enumerated(EnumType.STRING)
    private RainIntensity intensity;

    @Column(name = "confidence")
    private Double confidence;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RainEventStatus status;

    @Column(name = "processing_version")
    private String processingVersion;

    public RainEvent() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public Long getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public RainIntensity getIntensity() {
        return intensity;
    }

    public void setIntensity(RainIntensity intensity) {
        this.intensity = intensity;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public RainEventStatus getStatus() {
        return status;
    }

    public void setStatus(RainEventStatus status) {
        this.status = status;
    }

    public String getProcessingVersion() {
        return processingVersion;
    }

    public void setProcessingVersion(String processingVersion) {
        this.processingVersion = processingVersion;
    }
}