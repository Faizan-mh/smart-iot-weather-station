package com.weatherstation.backend.repository;

import com.weatherstation.backend.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorReadingRepository extends JpaRepository<SensorReading,Long> {
    boolean existsByDeviceIdAndMessageId(String deviceId, String messageId);
}
