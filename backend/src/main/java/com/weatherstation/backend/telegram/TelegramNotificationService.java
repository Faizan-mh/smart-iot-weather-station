package com.weatherstation.backend.telegram;

import com.weatherstation.backend.entity.Device;
import com.weatherstation.backend.entity.RainEvent;
import com.weatherstation.backend.processing.EventAssessment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TelegramNotificationService {

    private final TelegramClient telegramClient;
    private final TelegramChatRepository telegramChatRepository;

    public TelegramNotificationService(
            @Value("${telegram.bot.token}") String botToken, TelegramChatRepository telegramChatRepository) {

        this.telegramClient = new OkHttpTelegramClient(botToken);
        this.telegramChatRepository = telegramChatRepository;
    }

    public void sendMessage(Long chatId, String text) {

        SendMessage message = new SendMessage(
                chatId.toString(),
                text
        );

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            System.err.println(
                    "Failed to send Telegram message to chat "
                            + chatId + ": "
                            + e.getMessage()
            );
        }
    }
    public void sendToAllChats(String text) {
        telegramChatRepository.findAll()
                .forEach(chat -> sendMessage(chat.getChatId(), text));
    }

    public void notifyRainStarted(
            RainEvent event,
            EventAssessment assessment,
            Device device) {

        String message = """
            🌧️ Rain Started

            Device: %s
            Location: %s
            Start: %s
            Intensity: %s
            Active zones: %d/4
            Coverage: %.0f%%
            """.formatted(
                event.getDeviceId(),
                device.getLocation(),
                formatDateTime(event.getStartTime()),
                event.getPeakIntensity(),
                assessment.getActiveZoneCount(),
                assessment.getSpatialCoverage() * 100
        );

        sendToAllChats(message);
    }

    public void notifyRainEnded(RainEvent event, Device device) {

        String message = """
            🌤️ Rain Ended

            Device: %s
            Location: %s
            Started: %s
            Ended: %s
            Duration: %s
            Peak intensity: %s
            """.formatted(
                event.getDeviceId(),
                device.getLocation(),
                formatDateTime(event.getStartTime()),
                formatDateTime(event.getEndTime()),
                formatDuration(event.getDurationSeconds()),
                event.getPeakIntensity()
        );

        sendToAllChats(message);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }

        return dateTime.format(
                DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm:ss a")
        );
    }

    private String formatDuration(Long seconds) {
        if (seconds == null) {
            return "N/A";
        }

        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;

        return minutes + " min " + remainingSeconds + " sec";
    }
}