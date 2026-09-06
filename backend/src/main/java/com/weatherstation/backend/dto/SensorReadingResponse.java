package com.weatherstation.backend.dto;

import java.time.LocalDateTime;

public class SensorReadingResponse {
    private Long id;
    private String deviceId;
    private String messageId;
    private LocalDateTime deviceTimestamp;
    private LocalDateTime serverTimestamp;

    public SensorReadingResponse() {
    }

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

    public LocalDateTime getServerTimestamp() {
        return serverTimestamp;
    }

    public void setServerTimeStamp(LocalDateTime serverTimestamp) {
        this.serverTimestamp = serverTimestamp;
    }
}
