package com.weatherstation.backend.telegram;

import com.weatherstation.backend.entity.Device;
import com.weatherstation.backend.entity.EnvironmentalReading;
import com.weatherstation.backend.entity.RainEvent;
import com.weatherstation.backend.processing.EventAssessment;
import com.weatherstation.backend.repository.DeviceRepository;
import com.weatherstation.backend.repository.RainEventRepository;
import com.weatherstation.backend.service.RainEventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
public class SkyMetricsBot implements SpringLongPollingBot {

    private final String botToken;
    private final String botUsername;
    private final TelegramClient telegramClient;
    private final TelegramChatRepository telegramChatRepository;
    private final TelegramChatService telegramChatService;
    private final TelegramNotificationService telegramNotificationService;
    private final DeviceRepository deviceRepository;
    private final RainEventService rainEventService;
    private final RainEventRepository rainEventRepository;
    @Value("${telegram.default-device-id}")
    private String defaultDeviceId;

    public SkyMetricsBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            TelegramChatRepository telegramChatRepository, TelegramChatService telegramChatService, TelegramNotificationService telegramNotificationService, DeviceRepository deviceRepository, RainEventService rainEventService, RainEventRepository rainEventRepository) {

        this.botToken = botToken;
        this.botUsername = botUsername;
        this.telegramClient = new OkHttpTelegramClient(botToken);
        this.telegramChatRepository = telegramChatRepository;
        this.telegramChatService = telegramChatService;
        this.telegramNotificationService = telegramNotificationService;
        this.deviceRepository = deviceRepository;
        this.rainEventService = rainEventService;
        this.rainEventRepository = rainEventRepository;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {

        return updates -> {

            for (Update update : updates) {

                if (update.hasMessage()
                        && update.getMessage().hasText()) {

                    String messageText =
                            update.getMessage().getText().trim();

                    Long chatId =
                            update.getMessage().getChatId();

                    String chatType =
                            update.getMessage().getChat().getType();

                    String chatTitle;

                    if ("private".equals(chatType)) {

                        String firstName =
                                update.getMessage().getFrom().getFirstName();

                        String lastName =
                                update.getMessage().getFrom().getLastName();

                        chatTitle = firstName;

                        if (lastName != null && !lastName.isBlank()) {
                            chatTitle += " " + lastName;
                        }

                    } else {

                        chatTitle =
                                update.getMessage().getChat().getTitle();
                    }

                    telegramChatService.registerChat(
                            chatId,
                            chatType,
                            update.getMessage().getFrom().getUserName(),
                            chatTitle
                    );

                    switch (messageText) {

                        case "/start" -> sendStartMessage(chatId);

                        case "/help" -> sendHelpMessage(chatId);

                        case "/status" -> sendStatusMessage(chatId);

                        case "/recent" -> sendRecentEventsMessage(chatId);

                        default -> telegramNotificationService.sendMessage(
                                chatId,
                                "I don't recognize that command.\n\n"
                                        + "Use /help to see available commands."
                        );
                    }
                }
            }
        };
    }
    private void sendStartMessage(Long chatId) {

        String message = """
            🌦️ Welcome to SkyMetrics!

            Your chat has been registered successfully.

            I can provide:
            • Current weather and rain status
            • Recent rain events
            • Automatic weather updates
            • Rain start and end alerts

            Use /help to see available commands.
            """;

        telegramNotificationService.sendMessage(
                chatId,
                message
        );
    }
    private void sendHelpMessage(Long chatId) {

        String message = """
            🌦️ SkyMetrics Commands

            /start
            Register this chat and show the welcome message.

            /status
            Show the current weather and rain status.

            /recent
            Show the latest rain events.

            /help
            Show this help message.
            """;

        telegramNotificationService.sendMessage(
                chatId,
                message
        );
    }
    private void sendStatusMessage(Long chatId) {

        Optional<Device> device =
                Optional.ofNullable(deviceRepository.findByDeviceId(defaultDeviceId));

        if (device.isEmpty()) {

            telegramNotificationService.sendMessage(
                    chatId,
                    "⚠️ Weather station device was not found."
            );

            return;
        }

        Device currentDevice = device.get();

        Optional<EnvironmentalReading> environment =
                rainEventService.getLatestEnvironmentalReading(
                        defaultDeviceId
                );

        Optional<RainEvent> currentRainEvent =
                rainEventService.getCurrentRainEvent(
                        defaultDeviceId
                );

        EventAssessment assessment =
                rainEventService.getLatestEventAssessment(
                        defaultDeviceId
                );

        StringBuilder message = new StringBuilder();

        message.append("🌦️ SkyMetrics Status\n\n");

        message.append("📡 Device\n");
        message.append("ID: ")
                .append(currentDevice.getDeviceId())
                .append("\n");

        message.append("Location: ")
                .append(currentDevice.getLocation())
                .append("\n\n");

        message.append("🌡️ Environment\n");

        if (environment.isPresent()) {

            EnvironmentalReading reading =
                    environment.get();

            message.append("Temperature: ")
                    .append(reading.getTemperatureC())
                    .append(" °C\n");

            message.append("Humidity: ")
                    .append(reading.getHumidityPercent())
                    .append(" %\n");

            message.append("Pressure: ")
                    .append(reading.getPressureHpa())
                    .append(" hPa\n");

            message.append("Wind: ")
                    .append(reading.getWindSpeedKmh())
                    .append(" km/h\n");

            message.append("Gust: ")
                    .append(reading.getWindGustKmh())
                    .append(" km/h\n");

        } else {

            message.append("No environmental data available.\n");
        }

        message.append("\n🌧️ Rain\n");

        if (currentRainEvent.isPresent()) {

            RainEvent event =
                    currentRainEvent.get();

            message.append("Status: RAINING\n");

            message.append("Intensity: ")
                    .append(event.getPeakIntensity())
                    .append("\n");

            message.append("Started: ")
                    .append(formatDateTime(event.getStartTime()))
                    .append("\n");

            if (assessment != null) {

                message.append("Active zones: ")
                        .append(assessment.getActiveZoneCount())
                        .append("/4\n");

                message.append("Coverage: ")
                        .append(assessment.getSpatialCoverage() * 100)
                        .append(" %\n");
            }

        } else {

            message.append("Status: No rain\n");
        }

        telegramNotificationService.sendMessage(
                chatId,
                message.toString()
        );
    }
    private String formatDuration(Long seconds) {

        if (seconds == null) {
            return "N/A";
        }

        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        if (hours > 0) {
            return hours + "h "
                    + minutes + "m "
                    + remainingSeconds + "s";
        }

        return minutes + "m "
                + remainingSeconds + "s";
    }
    private void sendRecentEventsMessage(Long chatId) {

        List<RainEvent> events =
                rainEventRepository
                        .findByDeviceIdOrderByStartTimeDesc(
                                defaultDeviceId
                        );

        if (events.isEmpty()) {

            telegramNotificationService.sendMessage(
                    chatId,
                    "🌧️ No rain events have been recorded yet."
            );

            return;
        }

        StringBuilder message =
                new StringBuilder("🌧️ Recent Rain Events\n\n");

        events.stream()
                .limit(5)
                .forEach(event -> {

                    message.append("• ")
                            .append(formatDateTime(event.getStartTime()))
                            .append("\n");

                    message.append("  Status: ")
                            .append(event.getStatus())
                            .append("\n");

                    message.append("  Peak: ")
                            .append(event.getPeakIntensity())
                            .append("\n");

                    if (event.getEndTime() != null) {

                        message.append("  Ended: ")
                                .append(formatDateTime(event.getStartTime()))
                                .append("\n");

                        message.append("  Duration: ")
                                .append(formatDuration(
                                        event.getDurationSeconds()
                                ))
                                .append("\n");
                    }

                    message.append("\n");
                });

        telegramNotificationService.sendMessage(
                chatId,
                message.toString()
        );
    }
    private String formatDateTime(LocalDateTime dateTime) {

        if (dateTime == null) {
            return "N/A";
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm:ss a");

        return dateTime.format(formatter);
    }
}