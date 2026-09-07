package com.weatherstation.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class DeviceRequest {

    @NotBlank(message = "Enter a valid deviceId")
    private String deviceId;
    @NotBlank(message = "Enter a valid device name")
    private String name;
    @NotBlank(message = "Enter a valid location")
    private String location;

    public DeviceRequest() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
