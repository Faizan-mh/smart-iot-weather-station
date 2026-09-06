package com.weatherstation.backend.service;

import com.weatherstation.backend.dto.DeviceRequest;
import com.weatherstation.backend.dto.DeviceResponse;
import com.weatherstation.backend.entity.Device;
import com.weatherstation.backend.exception.DuplicateResourceException;
import com.weatherstation.backend.mapper.DeviceMapper;
import com.weatherstation.backend.repository.DeviceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    public DeviceService(DeviceRepository deviceRepository, DeviceMapper deviceMapper) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
    }

    public DeviceResponse createDevice(DeviceRequest deviceRequest){
        Device device = deviceMapper.toEntity(deviceRequest);

        if(deviceExists(device)){
            throw new DuplicateResourceException("Device Already Exists");
        }
        device.setCreatedAt(LocalDateTime.now());
        device.setActive(true);
        DeviceResponse DeviceResp = deviceMapper.toDto(deviceRepository.save(device));
        return DeviceResp;

    }

    private boolean deviceExists(Device device){
            Device deviceresp = deviceRepository.findByDeviceId(device.getDeviceId());
            if(deviceresp == null){
                return false;
            }
            return true;
    }
}
