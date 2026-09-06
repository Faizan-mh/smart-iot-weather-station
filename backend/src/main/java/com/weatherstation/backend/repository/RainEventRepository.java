package com.weatherstation.backend.repository;

import com.weatherstation.backend.entity.RainEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RainEventRepository extends JpaRepository<RainEvent,Long> {
}
