package com.weatherstation.backend.controller;

import com.weatherstation.backend.dto.*;
import com.weatherstation.backend.entity.RainEvent;
import com.weatherstation.backend.entity.RainEventTimeline;
import com.weatherstation.backend.enums.RainEventStatus;
import com.weatherstation.backend.mapper.RainEventMapper;
import com.weatherstation.backend.processing.EventAssessment;
import com.weatherstation.backend.repository.RainEventRepository;
import com.weatherstation.backend.repository.RainEventTimelineRepository;
import com.weatherstation.backend.service.RainEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1/rain-events")
public class RainEventController {

    public final RainEventRepository rainEventRepository;
    public final RainEventMapper rainEventMapper;
    public final RainEventTimelineRepository rainEventTimelineRepository;
    private final RainEventService rainEventService;

    public RainEventController(RainEventRepository rainEventRepository, RainEventMapper rainEventMapper, RainEventTimelineRepository rainEventTimelineRepository, RainEventService rainEventService) {
        this.rainEventRepository = rainEventRepository;
        this.rainEventMapper = rainEventMapper;
        this.rainEventTimelineRepository = rainEventTimelineRepository;
        this.rainEventService = rainEventService;
    }

    @GetMapping("/current/{deviceId}")
    public ResponseEntity<CurrentRainEventResponse> getCurrentEvent(@PathVariable String deviceId) {
        return rainEventRepository.
                findTopByDeviceIdAndStatusInOrderByStartTimeDesc(
                        deviceId,
                        List.of(RainEventStatus.ACTIVE,
                                RainEventStatus.ENDING))
                .map(rainEventMapper::toResponse)
                .map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    @GetMapping("/{deviceId}")
    public ResponseEntity<List<RainEventResponse>> getEventHistory(@PathVariable String deviceId) {
        List<RainEventResponse> events =
                rainEventRepository
                        .findByDeviceIdOrderByStartTimeDesc(deviceId)
                        .stream()
                        .map(rainEventMapper::toRainResponse)
                        .toList();
        return ResponseEntity.ok(events);
    }
    @GetMapping("/event/{eventId}")
    public ResponseEntity<RainEventDetailsResponse> getEventDetails(
            @PathVariable Long eventId) {

        Optional<RainEvent> eventOptional =
                rainEventRepository.findById(eventId);

        if (eventOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RainEvent event = eventOptional.get();

        List<RainEventTimelineResponse> timeline =
                rainEventTimelineRepository
                        .findByEventIdOrderByStartTimeAsc(eventId)
                        .stream()
                        .map(rainEventMapper::toTimelineResponse)
                        .toList();

        RainEventDetailsResponse response =
                rainEventMapper.toDetailsResponse(event, timeline);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/assessment/{deviceId}")
    public ResponseEntity<EventAssessmentResponse> getLatestAssessment(@PathVariable String deviceId) {
        EventAssessment assessment = rainEventService.getLatestEventAssessment(deviceId);
        if (assessment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rainEventMapper.toResponse(assessment));
    }


}
