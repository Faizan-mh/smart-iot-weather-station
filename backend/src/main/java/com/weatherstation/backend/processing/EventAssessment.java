package com.weatherstation.backend.processing;

public class EventAssessment {
    private EventClassification eventClassification;
    private int activeZoneCount;
    private double spatialCoverage;
    private boolean persistentPiezoActivity;
    private Boolean rainSensorWet;
    private Double windSpeedKmh;
    private Double windGustKmh;
    private Double temperatureC;
    private Double humidityPercent;
    private Double pressureHpa;
    private String reasoning;

    public EventAssessment() {
    }

    public int getActiveZoneCount() {
        return activeZoneCount;
    }

    public void setActiveZoneCount(int activeZoneCount) {
        this.activeZoneCount = activeZoneCount;
    }

    public double getSpatialCoverage() {
        return spatialCoverage;
    }

    public void setSpatialCoverage(double spatialCoverage) {
        this.spatialCoverage = spatialCoverage;
    }

    public boolean isPersistentPiezoActivity() {
        return persistentPiezoActivity;
    }

    public void setPersistentPiezoActivity(boolean persistentPiezoActivity) {
        this.persistentPiezoActivity = persistentPiezoActivity;
    }

    public Boolean isRainSensorWet() {
        return rainSensorWet;
    }

    public void setRainSensorWet(Boolean rainSensorWet) {
        this.rainSensorWet = rainSensorWet;
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

    public EventClassification getEventClassification() {
        return eventClassification;
    }

    public void setEventClassification(EventClassification eventClassification) {
        this.eventClassification = eventClassification;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }
}
