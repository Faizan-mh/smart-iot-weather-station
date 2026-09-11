package com.weatherstation.backend.service;

import com.weatherstation.backend.entity.EnvironmentalReading;
import com.weatherstation.backend.entity.RainEvent;
import com.weatherstation.backend.entity.SensorReading;
import com.weatherstation.backend.enums.RainEventStatus;
import com.weatherstation.backend.enums.RainIntensity;
import com.weatherstation.backend.processing.*;
import com.weatherstation.backend.repository.EnvironmentalReadingRepository;
import com.weatherstation.backend.repository.RainEventRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RainEventService {
    private final RainEventRepository rainEventRepository;
    private final PiezoAnalyzer piezoAnalyzer;
    private final CrossZoneAnalyzer crossZoneAnalyzer;
    private final EvidenceFusion evidenceFusion;
    private final IntensityClassifier intensityClassifier;
    private final EnvironmentalReadingRepository environmentalReadingRepository;
    private final Map<String, TemporalAnalyzer> temporalAnalyzers =
            new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> endingStartedAt =
            new ConcurrentHashMap<>();

    public RainEventService(RainEventRepository rainEventRepository,
                            PiezoAnalyzer piezoAnalyzer,
                            CrossZoneAnalyzer crossZoneAnalyzer,
                            EvidenceFusion evidenceFusion,
                            IntensityClassifier intensityClassifier,
                            EnvironmentalReadingRepository environmentalReadingRepository
    ) {
        this.rainEventRepository = rainEventRepository;
        this.piezoAnalyzer = piezoAnalyzer;
        this.crossZoneAnalyzer = crossZoneAnalyzer;
        this.evidenceFusion = evidenceFusion;
        this.intensityClassifier = intensityClassifier;
        this.environmentalReadingRepository = environmentalReadingRepository;
    }

    public EventAssessment processReading(SensorReading reading, Configuration configuration) {
        TemporalAnalyzer temporalAnalyzer = temporalAnalyzers.computeIfAbsent(reading.getDeviceId(), id -> new TemporalAnalyzer());

        boolean[] activeZones =
                piezoAnalyzer.analyzeZones(reading, configuration);

        int activeZonesCount =
                crossZoneAnalyzer.countActiveZones(activeZones);

        double spatialCoverage =
                crossZoneAnalyzer.calculateSpatialCoverage(activeZones);

        boolean persistentPiezoActivity =
                temporalAnalyzer.update(
                        activeZonesCount,
                        configuration.getMinimumActiveZones(),
                        configuration.getRequiredConsecutiveWindows()
                );
        EventAssessment eventAssessment = new EventAssessment();
        eventAssessment.setActiveZoneCount(activeZonesCount);
        eventAssessment.setSpatialCoverage(spatialCoverage);
        eventAssessment.setPersistentPiezoActivity(persistentPiezoActivity);

        Boolean rainSensorWet = null;

        if (reading.getRainSensor() != null) {
            rainSensorWet = reading.getRainSensor() <= configuration.getRainSensorWetThreshold();
        }

        eventAssessment.setRainSensorWet(rainSensorWet);

        Optional<EnvironmentalReading> latestEnvironmentalReading =
                environmentalReadingRepository
                        .findTopByDeviceIdOrderByDeviceTimestampDesc(
                                reading.getDeviceId());

        latestEnvironmentalReading.ifPresent(environment -> {
            eventAssessment.setTemperatureC(environment.getTemperatureC());
            eventAssessment.setHumidityPercent(environment.getHumidityPercent());
            eventAssessment.setPressureHpa(environment.getPressureHpa());
            eventAssessment.setWindSpeedKmh(environment.getWindSpeedKmh());
            eventAssessment.setWindGustKmh(environment.getWindGustKmh());

        });

        EventAssessment finalAssessment = evidenceFusion.fuse(eventAssessment, configuration);
        handleEventLifecycle(reading,finalAssessment,configuration);
        return finalAssessment;

    }

    private Optional<RainEvent> findCurrentEvent(String deviceId) {
        return rainEventRepository
                .findTopByDeviceIdAndStatusInOrderByStartTimeDesc(deviceId,
                        List.of(RainEventStatus.ACTIVE,RainEventStatus.ENDING));
    }
    private RainIntensity calculateIntensity(SensorReading reading, EventAssessment assessment, Configuration configuration) {
        double intensityScore = intensityClassifier.calculateIntensityScore(
                reading,
                assessment.getSpatialCoverage(),
                configuration);
        return intensityClassifier.classify(intensityScore, configuration);
    }

    private RainEvent createRainEvent(SensorReading reading, EventAssessment assessment, Configuration configuration) {
        RainEvent event = new RainEvent();
        event.setDeviceId(reading.getDeviceId());
        event.setStartTime(reading.getDeviceTimestamp());
        event.setStatus(RainEventStatus.ACTIVE);
        event.setIntensity(calculateIntensity(reading, assessment, configuration));
        event.setProcessingVersion("v1");
        return event;
    }
    private RainEvent startNewRainEvent(SensorReading reading, EventAssessment assessment,Configuration configuration) {
        return rainEventRepository.save(
                createRainEvent(
                        reading,
                        assessment,
                        configuration

                ));
    }
    private RainEvent updateActiveEvent(RainEvent event, EventAssessment assessment, SensorReading reading,Configuration configuration) {
        endingStartedAt.remove(reading.getDeviceId());
        event.setIntensity(calculateIntensity(reading, assessment, configuration));
        event.setStatus(RainEventStatus.ACTIVE);
        event.setEndTime(null);
        return rainEventRepository.save(event);
    }
    private RainEvent handleEventLifecycle(SensorReading reading, EventAssessment assessment, Configuration configuration) {
        Optional<RainEvent> currentEvent = findCurrentEvent(reading.getDeviceId());
        switch (assessment.getEventClassification()){
            case RAIN_CONFIRMED:
                if (currentEvent.isPresent()) {
                    return updateActiveEvent(
                            currentEvent.get(),
                            assessment,
                            reading,
                            configuration);
                }
                return startNewRainEvent(reading,
                        assessment,
                        configuration);
            case RAIN_CANDIDATE:
                    if (currentEvent.isPresent()) {
                        return updateActiveEvent(currentEvent.get(), assessment, reading, configuration);
                    }
                    return null;
            case NO_ACTIVITY:
                if (currentEvent.isPresent()) {
                    return handlePossibleEventEnding(currentEvent.get(),assessment,reading,configuration);
                }
                return null;
            default:
                return null;
        }
    }
    private RainEvent handlePossibleEventEnding(
            RainEvent event,
            EventAssessment assessment,
            SensorReading reading,
            Configuration configuration) {
        String deviceId = reading.getDeviceId();
        LocalDateTime currentTime = reading.getDeviceTimestamp();
        if(event.getStatus() != RainEventStatus.ENDING){
            event.setStatus(RainEventStatus.ENDING);
            endingStartedAt.put(deviceId, currentTime);
            return rainEventRepository.save(event);
        }
        LocalDateTime endingStart = endingStartedAt.get(deviceId);
        if(endingStart == null){
            endingStartedAt.put(deviceId, currentTime);
            return rainEventRepository.save(event);
        }
        long inactiveDurationSeconds = Duration.between(endingStart, currentTime).getSeconds();
        if(inactiveDurationSeconds >= configuration.getEndConfirmationSeconds()){
            event.setStatus(RainEventStatus.COMPLETED);
            event.setEndTime(currentTime);
            long eventDurationSeconds = Duration.between(event.getStartTime(), currentTime).getSeconds();
            event.setDurationSeconds(eventDurationSeconds);
            endingStartedAt.remove(deviceId);
            return rainEventRepository.save(event);
        }
        return event;
    }
}

