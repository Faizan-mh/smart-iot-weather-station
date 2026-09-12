package com.weatherstation.backend.repository;

import com.weatherstation.backend.entity.RainEventTimeline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RainEventTimelineRepository extends JpaRepository<RainEventTimeline, Long> {
    Optional<RainEventTimeline> findTopByEventIdOrderByStartTimeDesc(
            Long eventId);

    List<RainEventTimeline> findByEventIdOrderByStartTimeAsc(
            Long eventId);
}
