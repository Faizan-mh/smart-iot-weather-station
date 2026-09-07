package com.weatherstation.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class EnvironmentalReadingRequest {

    @NotBlank(message = "Enter a valid deviceId")
    private String deviceId;
    @NotBlank(message = "Enter valid timestamp")
    private LocalDateTime deviceTimestamp;

    @NotBlank(message = "Enter valid temperature")
    private Double temperatureC;
    @NotBlank(message = "Enter Valid HumidityPercent")
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
