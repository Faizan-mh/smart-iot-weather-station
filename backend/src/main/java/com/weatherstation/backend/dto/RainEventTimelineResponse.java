package com.weatherstation.backend.dto;

import com.weatherstation.backend.enums.RainIntensity;

import java.time.LocalDateTime;

public class RainEventTimelineResponse {
    private LocalDateTime timestamp;
    private RainIntensity rainIntensity;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public RainIntensity getRainIntensity() {
        return rainIntensity;
    }

    public void setRainIntensity(RainIntensity rainIntensity) {
        this.rainIntensity = rainIntensity;
    }
}
