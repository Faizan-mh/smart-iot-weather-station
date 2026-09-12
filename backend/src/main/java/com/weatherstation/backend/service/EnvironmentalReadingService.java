package com.weatherstation.backend.service;

import com.weatherstation.backend.dto.EnvironmentalReadingRequest;
import com.weatherstation.backend.dto.EnvironmentalReadingResponse;
import com.weatherstation.backend.entity.EnvironmentalReading;
import com.weatherstation.backend.exception.ResourceNotFoundException;
import com.weatherstation.backend.mapper.EnvironmentalReadingMapper;
import com.weatherstation.backend.repository.DeviceRepository;
import com.weatherstation.backend.repository.EnvironmentalReadingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvironmentalReadingService {

    private final EnvironmentalReadingRepository repository;
    private final DeviceRepository deviceRepository;
    private final EnvironmentalReadingMapper mapper;

    public EnvironmentalReadingService(
            EnvironmentalReadingRepository repository,
            DeviceRepository deviceRepository,
            EnvironmentalReadingMapper mapper) {

        this.repository = repository;
        this.deviceRepository = deviceRepository;
        this.mapper = mapper;
    }

    public EnvironmentalReadingResponse createReading(
            EnvironmentalReadingRequest request) {

        if (!deviceRepository.existsByDeviceId(request.getDeviceId())) {
            throw new ResourceNotFoundException(
                    "Device not found: " + request.getDeviceId()
            );
        }

        EnvironmentalReading reading =
                mapper.toEntity(request);

        EnvironmentalReading savedReading =
                repository.save(reading);

        return mapper.toResponse(savedReading);
    }

    public EnvironmentalReadingResponse getLatestReading(String deviceId) {

        EnvironmentalReading reading =
                repository.findTopByDeviceIdOrderByDeviceTimestampDesc(deviceId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No environmental reading found for device: "
                                                + deviceId
                                ));

        return mapper.toResponse(reading);
    }
    public List<EnvironmentalReadingResponse> getReadingHistory(
            String deviceId) {

        return repository
                .findByDeviceIdOrderByDeviceTimestampDesc(deviceId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
