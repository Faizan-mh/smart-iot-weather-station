package com.weatherstation.backend.mapper;

import com.weatherstation.backend.dto.*;
import com.weatherstation.backend.entity.RainEvent;
import com.weatherstation.backend.entity.RainEventTimeline;
import com.weatherstation.backend.processing.EventAssessment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RainEventMapper {
    public CurrentRainEventResponse toResponse(RainEvent rainEvent) {
        CurrentRainEventResponse response = new CurrentRainEventResponse();
        response.setId(rainEvent.getId());
        response.setDeviceId(rainEvent.getDeviceId());
        response.setStartTime(rainEvent.getStartTime());
        response.setPeakIntensity(rainEvent.getPeakIntensity());
        response.setStatus(rainEvent.getStatus());
        response.setProcessingVersion(rainEvent.getProcessingVersion());
        return response;
    }

    public EventAssessmentResponse toResponse(EventAssessment assessment) {
        EventAssessmentResponse response = new EventAssessmentResponse();
        response.setEventClassification(assessment.getEventClassification());
        response.setActiveZoneCount(assessment.getActiveZoneCount());
        response.setSpatialCoverage(assessment.getSpatialCoverage());
        response.setPersistentPiezoActivity(
                assessment.isPersistentPiezoActivity()
        );
        response.setRainSensorWet(assessment.isRainSensorWet());
        response.setWindSpeedKmh(assessment.getWindSpeedKmh());
        response.setWindGustKmh(assessment.getWindGustKmh());
        response.setTemperatureC(assessment.getTemperatureC());
        response.setHumidityPercent(assessment.getHumidityPercent());
        response.setPressureHpa(assessment.getPressureHpa());
        response.setReasoning(assessment.getReasoning());

        return response;
    }
    public RainEventResponse toRainResponse(RainEvent rainEvent) {
        RainEventResponse response = new RainEventResponse();
        response.setId(rainEvent.getId());
        response.setDeviceId(rainEvent.getDeviceId());
        response.setStartTime(rainEvent.getStartTime());
        response.setEndTime(rainEvent.getEndTime());
        response.setDurationSeconds(rainEvent.getDurationSeconds());
        response.setPeakIntensity(rainEvent.getPeakIntensity());
        response.setStatus(rainEvent.getStatus());
        response.setProcessingVersion(rainEvent.getProcessingVersion());
        return response;
    }

    public RainEventDetailsResponse toDetailsResponse(
            RainEvent event,
            List<RainEventTimelineResponse> timeline) {

        RainEventDetailsResponse response = new RainEventDetailsResponse();

        response.setId(event.getId());
        response.setDeviceId(event.getDeviceId());
        response.setStartTime(event.getStartTime());
        response.setEndTime(event.getEndTime());
        response.setDurationSeconds(event.getDurationSeconds());
        response.setPeakIntensity(event.getPeakIntensity());
        response.setStatus(event.getStatus());
        response.setProcessingVersion(event.getProcessingVersion());
        response.setTimeline(timeline);

        return response;
    }
    public RainEventTimelineResponse toTimelineResponse(
            RainEventTimeline timeline) {

        RainEventTimelineResponse response =
                new RainEventTimelineResponse();

        response.setTimestamp(timeline.getStartTime());
        response.setRainIntensity(timeline.getIntensity());

        return response;
    }

}