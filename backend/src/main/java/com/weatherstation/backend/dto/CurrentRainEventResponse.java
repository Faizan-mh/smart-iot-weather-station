package com.weatherstation.backend.dto;

import com.weatherstation.backend.enums.RainEventStatus;
import com.weatherstation.backend.enums.RainIntensity;

import java.time.LocalDateTime;

public class CurrentRainEventResponse {

    private Long id;
    private String deviceId;
    private LocalDateTime startTime;
    private RainIntensity peakIntensity;
    private RainEventStatus status;
    private String processingVersion;

    public CurrentRainEventResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public RainIntensity getPeakIntensity() {
        return peakIntensity;
    }

    public void setPeakIntensity(RainIntensity peakIntensity) {
        this.peakIntensity = peakIntensity;
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
