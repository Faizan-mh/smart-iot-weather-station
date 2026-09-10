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

    private double pressureHpa;
    private double windSpeedKmh;
    private double windGustKmh;

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
