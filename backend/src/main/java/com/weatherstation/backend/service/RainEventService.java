package com.weatherstation.backend.service;

import com.weatherstation.backend.repository.RainEventRepository;
import org.springframework.stereotype.Service;

@Service
public class RainEventService {
    private final RainEventRepository rainEventRepository;

    public RainEventService(RainEventRepository rainEventRepository) {
        this.rainEventRepository = rainEventRepository;
    }
}
