package com.weatherstation.backend.service;

import com.weatherstation.backend.repository.EnvironmentalReadingRepository;
import org.springframework.stereotype.Service;

@Service
public class EnvironmentalReadingService {
    private final EnvironmentalReadingRepository environmentalReadingRepository;

    public EnvironmentalReadingService(EnvironmentalReadingRepository environmentalReadingRepository) {
        this.environmentalReadingRepository = environmentalReadingRepository;
    }
}
