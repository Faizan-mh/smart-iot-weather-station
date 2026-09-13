package com.weatherstation.backend.telegram;

import com.weatherstation.backend.entity.Device;
import com.weatherstation.backend.entity.EnvironmentalReading;
import com.weatherstation.backend.entity.RainEvent;
import com.weatherstation.backend.repository.DeviceRepository;
import com.weatherstation.backend.service.RainEventService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class TelegramWeatherScheduler {

    private final TelegramNotificationService telegramNotificationService;
    private final DeviceRepository deviceRepository;
    private final RainEventService rainEventService;

    private final String defaultDeviceId;

    public TelegramWeatherScheduler(
            TelegramNotificationService telegramNotificationService,
            DeviceRepository deviceRepository,
            RainEventService rainEventService,
            @org.springframework.beans.factory.annotation.Value("${telegram.default-device-id}")
            String defaultDeviceId) {

        this.telegramNotificationService = telegramNotificationService;
        this.deviceRepository = deviceRepository;
        this.rainEventService = rainEventService;
        this.defaultDeviceId = defaultDeviceId;
    }

    @Scheduled(
            fixedRateString = "${telegram.weather-update-interval-ms:7200000}"
    )
    public void sendWeatherSummary() {

        Device device = deviceRepository.findByDeviceId(defaultDeviceId);

        if (device == null) {
            return;
        }

        Optional<EnvironmentalReading> environment =
                rainEventService.getLatestEnvironmentalReading(defaultDeviceId);

        if (environment.isEmpty()) {
            return;
        }

        Optional<RainEvent> currentRain =
                rainEventService.getCurrentRainEvent(defaultDeviceId);

        String rainStatus = currentRain.isPresent()
                ? "RAINING"
                : "NO RAIN";

        EnvironmentalReading reading = environment.get();

        String message = """
                🌦️ SkyMetrics Weather Update

                Device: %s
                Location: %s

                🌡️ Temperature: %.1f °C
                💧 Humidity: %.1f %%
                🌬️ Pressure: %.1f hPa
                💨 Wind: %.1f km/h
                💨 Gust: %.1f km/h

                🌧️ Rain: %s

                Updated: %s
                """.formatted(
                device.getDeviceId(),
                device.getLocation(),
                reading.getTemperatureC(),
                reading.getHumidityPercent(),
                reading.getPressureHpa(),
                reading.getWindSpeedKmh(),
                reading.getWindGustKmh(),
                rainStatus,
                formatDateTime(reading.getDeviceTimestamp())
        );

        telegramNotificationService.sendToAllChats(message);
    }

    private String formatDateTime(
            java.time.LocalDateTime dateTime) {

        if (dateTime == null) {
            return "N/A";
        }

        return dateTime.format(
                DateTimeFormatter.ofPattern(
                        "dd MMM yyyy, hh:mm:ss a"
                )
        );
    }
}