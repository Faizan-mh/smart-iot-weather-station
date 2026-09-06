package com.weatherstation.backend.dto;

import java.time.LocalDateTime;

public class EnvironmentalReadingRequest {
    private String deviceId;
    private LocalDateTime deviceTimestamp;
    private Double temperatureC;
    private Double humidityPercent;

    public EnvironmentalReadingRequest() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public LocalDateTime getDeviceTimestamp() {
        return deviceTimestamp;
    }

    public void setDeviceTimestamp(LocalDateTime deviceTimestamp) {
        this.deviceTimestamp = deviceTimestamp;
    }

    public Double getTemperatureC() {
        return temperatureC;
    }

    public void setTemperatureC(Double temperatureC) {
        this.temperatureC = temperatureC;
    }

    public Double getHumidityPercent() {
        return humidityPercent;
    }

    public void setHumidityPercent(Double humidityPercent) {
        this.humidityPercent = humidityPercent;
    }
}
