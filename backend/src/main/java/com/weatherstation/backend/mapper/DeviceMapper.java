package com.weatherstation.backend.mapper;

import com.weatherstation.backend.dto.DeviceRequest;
import com.weatherstation.backend.dto.DeviceResponse;
import com.weatherstation.backend.entity.Device;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapper {
    public Device toEntity(DeviceRequest request){
        Device device = new Device();

        device.setDeviceId(request.getDeviceId());
        device.setName(request.getName());
        device.setLocation(request.getLocation());
        return device;
    }

    public DeviceResponse toDto(Device response){
        DeviceResponse deviceResponse = new DeviceResponse();

        deviceResponse.setDeviceId(response.getDeviceId());
        deviceResponse.setName(response.getName());
        deviceResponse.setLocation(response.getLocation());
        deviceResponse.setActive(response.isActive());
        return deviceResponse;
    }
}
