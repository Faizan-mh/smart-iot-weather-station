package com.weatherstation.backend.service;

import com.weatherstation.backend.dto.LiveWeatherUpdate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class LiveWeatherUpdateService {
    private final SimpMessagingTemplate messagingTemplate;

    public LiveWeatherUpdateService(SimpMessagingTemplate simpMessagingTemplate) {
        this.messagingTemplate = simpMessagingTemplate;
    }
    public void publish(String deviceId, LiveWeatherUpdate update) {
        String destination =
                "/topic/weather" + deviceId;
        messagingTemplate.convertAndSend(destination, update);
    }
}
