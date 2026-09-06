package com.weatherstation.backend.repository;

import com.weatherstation.backend.entity.EnvironmentalReading;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvironmentalReadingRepository extends JpaRepository<EnvironmentalReading,Long> {
}
