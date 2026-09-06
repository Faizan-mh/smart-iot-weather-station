package com.weatherstation.backend.service;

import com.weatherstation.backend.repository.SensorReadingRepository;
import org.springframework.stereotype.Service;

@Service
public class SensorReadingService {
    private final SensorReadingRepository sensorReadingRepository;

    public SensorReadingService(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }
}
