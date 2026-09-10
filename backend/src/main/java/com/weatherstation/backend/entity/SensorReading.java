package com.weatherstation.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_reading")
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "deviceTimestamp", nullable = false)
    private LocalDateTime deviceTimestamp;

    @Column(name = "serverTimestamp", nullable = false)
    private LocalDateTime serverTimestamp;

    @Column(name = "message_id")
    private String messageId;

    @Column(name = "rain_sensor")
    private Double rainSensor;

    @Column(name = "p1_peak")
    private Double p1Peak;

    @Column(name = "p2_peak")
    private Double p2Peak;

    @Column(name = "p3_peak")
    private Double p3Peak;

    @Column(name = "p4_peak")
    private Double p4Peak;

    @Column(name = "p1_rms")
    private Double p1Rms;

    @Column(name = "p2_rms")
    private Double p2Rms;

    @Column(name = "p3_rms")
    private Double p3Rms;

    @Column(name = "p4_rms")
    private Double p4Rms;

    @Column(name = "p1_impact_count")
    private Integer p1ImpactCount;

    @Column(name = "p2_impact_count")
    private Integer p2ImpactCount;

    @Column(name = "p3_impact_count")
    private Integer p3ImpactCount;

    @Column(name = "p4_impact_count")
    private Integer p4ImpactCount;

    @Column(name= "sampling_rate_hz")
    private double samplingRateHz;
    @Column(name = "window_duration_ms")
    private double windowDurationMs;

    public SensorReading() {
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

    public LocalDateTime getServerTimestamp() {
        return serverTimestamp;
    }

    public void setServerTimestamp(LocalDateTime serverTimestamp) {
        this.serverTimestamp = serverTimestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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

    public double getSamplingRateHz() {
        return samplingRateHz;
    }

    public void setSamplingRateHz(double samplingRateHz) {
        this.samplingRateHz = samplingRateHz;
    }

    public double getWindowDurationMs() {
        return windowDurationMs;
    }

    public void setWindowDurationMs(double windowDurationMs) {
        this.windowDurationMs = windowDurationMs;
    }
}
