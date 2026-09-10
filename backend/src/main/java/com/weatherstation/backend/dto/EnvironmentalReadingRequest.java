package com.weatherstation.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class EnvironmentalReadingRequest {

    @NotBlank(message = "Enter a valid deviceId")
    private String deviceId;
    @NotBlank(message = "Enter a valid timestamp")
    private LocalDateTime deviceTimestamp;

    @NotBlank(message = "Enter a valid temperature")
    private Double temperatureC;
    @NotBlank(message = "Enter a valid HumidityPercent")
    private Double humidityPercent;
    @NotBlank
    private double pressureHpa;
    @NotBlank
    private double windSpeedKmh;
    @NotBlank
    private double windGustKmh;

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

    public double getPressureHpa() {
        return pressureHpa;
    }

    public void setPressureHpa(double pressureHpa) {
        this.pressureHpa = pressureHpa;
    }

    public double getWindSpeedKmh() {
        return windSpeedKmh;
    }

    public void setWindSpeedKmh(double windSpeedKmh) {
        this.windSpeedKmh = windSpeedKmh;
    }

    public double getWindGustKmh() {
        return windGustKmh;
    }

    public void setWindGustKmh(double windGustKmh) {
        this.windGustKmh = windGustKmh;
    }
}
