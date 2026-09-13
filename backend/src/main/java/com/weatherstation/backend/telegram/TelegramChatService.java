package com.weatherstation.backend.telegram;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TelegramChatService {

    private final TelegramChatRepository telegramChatRepository;

    public TelegramChatService(
            TelegramChatRepository telegramChatRepository) {

        this.telegramChatRepository = telegramChatRepository;
    }

    public TelegramChat registerChat(
            Long chatId,
            String chatType,
            String username,
            String chatTitle) {

        return telegramChatRepository.findByChatId(chatId)
                .map(chat -> {

                    chat.setUsername(username);
                    chat.setChatTitle(chatTitle);

                    return telegramChatRepository.save(chat);

                })
                .orElseGet(() -> {

                    TelegramChat chat = new TelegramChat();

                    chat.setChatId(chatId);
                    chat.setChatType(chatType);
                    chat.setUsername(username);
                    chat.setChatTitle(chatTitle);
                    chat.setCreatedAt(LocalDateTime.now());

                    return telegramChatRepository.save(chat);
                });
    }
}