package com.weatherstation.backend.dto;

import java.time.LocalDateTime;

public class EnvironmentalReadingResponse {

        private Long id;
        private String deviceId;
        private LocalDateTime deviceTimestamp;
        private Double temperatureC;
        private Double humidityPercent;
        private Double pressureHpa;
        private Double windSpeedKmh;
        private Double windGustKmh;

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

    public Double getPressureHpa() {
        return pressureHpa;
    }

    public void setPressureHpa(Double pressureHpa) {
        this.pressureHpa = pressureHpa;
    }

    public Double getWindSpeedKmh() {
        return windSpeedKmh;
    }

    public void setWindSpeedKmh(Double windSpeedKmh) {
        this.windSpeedKmh = windSpeedKmh;
    }

    public Double getWindGustKmh() {
        return windGustKmh;
    }

    public void setWindGustKmh(Double windGustKmh) {
        this.windGustKmh = windGustKmh;
    }
}
