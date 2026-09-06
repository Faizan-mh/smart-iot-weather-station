package com.weatherstation.backend.dto;

import java.time.LocalDateTime;

public class SensorReadingRequest {
    private String deviceId;
    private String messageId;
    private LocalDateTime deviceTimestamp;

    private Double rainSensor;

    private Double p1Peak;
    private Double p2Peak;
    private Double p3Peak;
    private Double p4Peak;

    private Double p1Rms;
    private Double p2Rms;
    private Double p3Rms;
    private Double p4Rms;

    private Integer p1ImpactCount;
    private Integer p2ImpactCount;
    private Integer p3ImpactCount;
    private Integer p4ImpactCount;

    public SensorReadingRequest() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public LocalDateTime getDeviceTimestamp() {
        return deviceTimestamp;
    }

    public void setDeviceTimestamp(LocalDateTime deviceTimestamp) {
        this.deviceTimestamp = deviceTimestamp;
    }

    public Double getRainSensor() {
        return rainSensor;
    }

    public void setRainSensor(Double rainSensor) {
        this.rainSensor = rainSensor;
    }

    public Double getP1Peak() {
        return p1Peak;
    }

    public void setP1Peak(Double p1Peak) {
        this.p1Peak = p1Peak;
    }

    public Double getP2Peak() {
        return p2Peak;
    }

    public void setP2Peak(Double p2Peak) {
        this.p2Peak = p2Peak;
    }

    public Double getP3Peak() {
        return p3Peak;
    }

    public void setP3Peak(Double p3Peak) {
        this.p3Peak = p3Peak;
    }

    public Double getP4Peak() {
        return p4Peak;
    }

    public void setP4Peak(Double p4Peak) {
        this.p4Peak = p4Peak;
    }

    public Double getP1Rms() {
        return p1Rms;
    }

    public void setP1Rms(Double p1Rms) {
        this.p1Rms = p1Rms;
    }

    public Double getP2Rms() {
        return p2Rms;
    }

    public void setP2Rms(Double p2Rms) {
        this.p2Rms = p2Rms;
    }

    public Double getP3Rms() {
        return p3Rms;
    }

    public void setP3Rms(Double p3Rms) {
        this.p3Rms = p3Rms;
    }

    public Double getP4Rms() {
        return p4Rms;
    }

    public void setP4Rms(Double p4Rms) {
        this.p4Rms = p4Rms;
    }

    public Integer getP1ImpactCount() {
        return p1ImpactCount;
    }

    public void setP1ImpactCount(Integer p1ImpactCount) {
        this.p1ImpactCount = p1ImpactCount;
    }

    public Integer getP2ImpactCount() {
        return p2ImpactCount;
    }

    public void setP2ImpactCount(Integer p2ImpactCount) {
        this.p2ImpactCount = p2ImpactCount;
    }

    public Integer getP3ImpactCount() {
        return p3ImpactCount;
    }

    public void setP3ImpactCount(Integer p3ImpactCount) {
        this.p3ImpactCount = p3ImpactCount;
    }

    public Integer getP4ImpactCount() {
        return p4ImpactCount;
    }

    public void setP4ImpactCount(Integer p4ImpactCount) {
        this.p4ImpactCount = p4ImpactCount;
    }
}
