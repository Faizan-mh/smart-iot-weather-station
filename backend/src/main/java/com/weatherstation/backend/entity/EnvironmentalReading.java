package com.weatherstation.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "environmental_reading")
public class EnvironmentalReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "device_timestamp", nullable = false)
    private LocalDateTime deviceTimestamp;

    @Column(name = "temperature_c")
    private Double temperatureC;

    @Column(name = "humidity_percent")
    private Double humidityPercent;

    @Column(name = "pressure_hpa")
    private Double pressureHpa;
    @Column(name = "wind_speed_kmh")
    private Double windSpeedKmh;
    @Column(name = "wind_gust_kmh")
    private Double windGustKmh;

    public EnvironmentalReading() {
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
