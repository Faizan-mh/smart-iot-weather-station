package com.weatherstation.backend.service;

import com.weatherstation.backend.dto.SensorReadingRequest;
import com.weatherstation.backend.dto.SensorReadingResponse;
import com.weatherstation.backend.entity.SensorReading;
import com.weatherstation.backend.exception.ResourceNotFoundException;
import com.weatherstation.backend.exception.DuplicateResourceException;
import com.weatherstation.backend.mapper.SensorReadingMapper;
import com.weatherstation.backend.processing.Configuration;
import com.weatherstation.backend.processing.ConfigurationProvider;
import com.weatherstation.backend.repository.DeviceRepository;
import com.weatherstation.backend.repository.SensorReadingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SensorReadingService {
    private final SensorReadingRepository sensorReadingRepository;
    private final DeviceRepository deviceRepository;
    private final SensorReadingMapper sensorReadingMapper;
    private final RainEventService rainEventService;
    private final ConfigurationProvider configurationProvider;

    public SensorReadingService(
            SensorReadingRepository sensorReadingRepository,
            DeviceRepository deviceRepository,
            SensorReadingMapper sensorReadingMapper,
            RainEventService rainEventService,
            ConfigurationProvider configurationProvider
    ) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.deviceRepository = deviceRepository;
        this.sensorReadingMapper = sensorReadingMapper;
        this.rainEventService = rainEventService;
        this.configurationProvider = configurationProvider;
    }

    public SensorReadingResponse createSensorReading(SensorReadingRequest request) {
        SensorReading sensorReading =
                sensorReadingMapper.toEntity(request);

        if (deviceRepository.findByDeviceId(sensorReading.getDeviceId()) == null) {
            throw new ResourceNotFoundException(
                    "No device with device id " + sensorReading.getDeviceId() + " exists");
        }
        if (sensorReadingRepository.existsByDeviceIdAndMessageId(
                sensorReading.getDeviceId(),
                sensorReading.getMessageId())) {
            throw new DuplicateResourceException("Sensor reading already exists");
        }
        sensorReading.setServerTimestamp(LocalDateTime.now());
        SensorReading savedReading =
                sensorReadingRepository.save(sensorReading);
        Configuration configuration =
                configurationProvider.getConfiguration();

        rainEventService.processReading(
                savedReading,
                configuration
        );

        return sensorReadingMapper.toDto(savedReading);
    }
}
