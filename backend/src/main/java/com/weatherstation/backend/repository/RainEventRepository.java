package com.weatherstation.backend.repository;

import com.weatherstation.backend.entity.RainEvent;
import com.weatherstation.backend.enums.RainEventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface RainEventRepository extends JpaRepository<RainEvent,Long> {
    Optional<RainEvent> findTopByDeviceIdAndStatusInOrderByStartTimeDesc(
            String deviceId,
            Collection<RainEventStatus> statuses
    );
}
