package com.weatherstation.backend.telegram;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TelegramChatRepository extends JpaRepository<TelegramChat, Long> {
    Optional<TelegramChat> findByChatId(Long chatId);
}
