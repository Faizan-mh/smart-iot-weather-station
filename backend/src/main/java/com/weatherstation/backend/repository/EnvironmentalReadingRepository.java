package com.weatherstation.backend.repository;

import com.weatherstation.backend.entity.EnvironmentalReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnvironmentalReadingRepository extends JpaRepository<EnvironmentalReading,Long> {

    Optional<EnvironmentalReading> findTopByDeviceIdOrderByDeviceTimestampDesc(
            String deviceId
    );
    List<EnvironmentalReading> findByDeviceIdOrderByDeviceTimestampDesc(
            String deviceId);
}
