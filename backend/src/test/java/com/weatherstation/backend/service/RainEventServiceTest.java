package com.weatherstation.backend.service;

import com.weatherstation.backend.dto.CurrentRainEventResponse;
import com.weatherstation.backend.dto.EventAssessmentResponse;
import com.weatherstation.backend.dto.LiveWeatherUpdate;
import com.weatherstation.backend.entity.RainEvent;
import com.weatherstation.backend.entity.SensorReading;
import com.weatherstation.backend.enums.RainEventStatus;
import com.weatherstation.backend.enums.RainIntensity;
import com.weatherstation.backend.mapper.EnvironmentalReadingMapper;
import com.weatherstation.backend.mapper.RainEventMapper;
import com.weatherstation.backend.processing.*;
import com.weatherstation.backend.repository.EnvironmentalReadingRepository;
import com.weatherstation.backend.repository.RainEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RainEventServiceTest {

    @Mock
    private RainEventRepository rainEventRepository;

    @Mock
    private PiezoAnalyzer piezoAnalyzer;

    @Mock
    private CrossZoneAnalyzer crossZoneAnalyzer;

    @Mock
    private EvidenceFusion evidenceFusion;

    @Mock
    private IntensityClassifier intensityClassifier;

    @Mock
    private EnvironmentalReadingRepository environmentalReadingRepository;

    @Mock
    private RainEventTimelineService rainEventTimelineService;

    @Mock
    private LiveWeatherUpdateService liveWeatherUpdateService;
    @Mock
    private RainEventMapper rainEventMapper;
    @Mock
    private EnvironmentalReadingMapper environmentalReadingMapper;

    @InjectMocks
    private RainEventService rainEventService;

    private Configuration configuration() {
        Configuration configuration = new Configuration();

        configuration.setP1PeakThreshold(100);
        configuration.setP2PeakThreshold(100);
        configuration.setP3PeakThreshold(100);
        configuration.setP4PeakThreshold(100);

        configuration.setP1RmsThreshold(30);
        configuration.setP2RmsThreshold(30);
        configuration.setP3RmsThreshold(30);
        configuration.setP4RmsThreshold(30);

        configuration.setP1ImpactThreshold(3);
        configuration.setP2ImpactThreshold(3);
        configuration.setP3ImpactThreshold(3);
        configuration.setP4ImpactThreshold(3);

        configuration.setMinimumActiveZones(2);
        configuration.setRequiredConsecutiveWindows(3);

        configuration.setRainSensorWetThreshold(500);
        configuration.setHighWindThresholdKmh(20);

        configuration.setEndConfirmationSeconds(30);

        configuration.setImpactFrequencyReference(10);
        configuration.setRmsReference(50);
        configuration.setModerateIntensityThreshold(0.40);
        configuration.setHeavyIntensityThreshold(0.70);

        return configuration;
    }

    @Test
    void confirmedRainShouldStartNewEvent() {

        Configuration configuration = configuration();

        SensorReading reading = new SensorReading();
        reading.setDeviceId("WS-001");
        reading.setDeviceTimestamp(LocalDateTime.now());

        reading.setRainSensor(200.0);

        reading.setP1Peak(150.0);
        reading.setP2Peak(160.0);
        reading.setP3Peak(140.0);
        reading.setP4Peak(155.0);

        reading.setP1Rms(40.0);
        reading.setP2Rms(42.0);
        reading.setP3Rms(38.0);
        reading.setP4Rms(41.0);

        reading.setP1ImpactCount(5);
        reading.setP2ImpactCount(6);
        reading.setP3ImpactCount(4);
        reading.setP4ImpactCount(5);

        reading.setSamplingRateHz(100.0);
        reading.setWindowDurationMs(1000.0);

        EventAssessment assessment = new EventAssessment();
        assessment.setEventClassification(
                EventClassification.RAIN_CONFIRMED
        );
        assessment.setActiveZoneCount(4);
        assessment.setSpatialCoverage(1.0);
        assessment.setPersistentPiezoActivity(true);
        assessment.setRainSensorWet(true);
        assessment.setReasoning("Strong rain evidence");

        when(piezoAnalyzer.analyzeZones(
                reading,
                configuration
        )).thenReturn(
                new boolean[]{true, true, true, true}
        );

        // Cross-zone analysis
        when(crossZoneAnalyzer.countActiveZones(
                any(boolean[].class)
        )).thenReturn(4);

        when(crossZoneAnalyzer.calculateSpatialCoverage(
                any(boolean[].class)
        )).thenReturn(1.0);

        // No environmental reading available
        when(environmentalReadingRepository
                .findTopByDeviceIdOrderByDeviceTimestampDesc("WS-001"))
                .thenReturn(Optional.empty());

        // Evidence fusion
        when(evidenceFusion.fuse(
                any(EventAssessment.class),
                eq(configuration)
        )).thenReturn(assessment);

        when(rainEventRepository
                .findTopByDeviceIdAndStatusInOrderByStartTimeDesc(
                        eq("WS-001"),
                        anyList()
                ))
                .thenReturn(Optional.empty());

        // Intensity calculation
        when(intensityClassifier.calculateIntensityScore(
                reading,
                1.0,
                configuration
        )).thenReturn(0.6);

        // Intensity classification
        when(intensityClassifier.classify(
                0.6,
                configuration
        )).thenReturn(RainIntensity.MODERATE);

        // Simulate database save
        RainEvent savedEvent = new RainEvent();
        savedEvent.setId(1L);
        savedEvent.setDeviceId("WS-001");
        savedEvent.setStatus(RainEventStatus.ACTIVE);
        savedEvent.setPeakIntensity(RainIntensity.MODERATE);
        savedEvent.setStartTime(reading.getDeviceTimestamp());

        when(rainEventRepository.save(
                any(RainEvent.class)
        )).thenReturn(savedEvent);

        // WebSocket mapper responses
        EventAssessmentResponse assessmentResponse =
                new EventAssessmentResponse();

        CurrentRainEventResponse eventResponse =
                new CurrentRainEventResponse();

        when(rainEventMapper.toResponse(
                assessment
        )).thenReturn(assessmentResponse);

        when(rainEventMapper.toResponse(
                any(RainEvent.class)
        )).thenReturn(eventResponse);

        EventAssessment result =
                rainEventService.processReading(
                        reading,
                        configuration
                );

        assertEquals(
                EventClassification.RAIN_CONFIRMED,
                result.getEventClassification()
        );

        ArgumentCaptor<RainEvent> eventCaptor =
                ArgumentCaptor.forClass(RainEvent.class);

        verify(rainEventRepository)
                .save(eventCaptor.capture());

        RainEvent capturedEvent =
                eventCaptor.getValue();

        assertEquals(
                "WS-001",
                capturedEvent.getDeviceId()
        );

        assertEquals(
                RainEventStatus.ACTIVE,
                capturedEvent.getStatus()
        );

        assertEquals(
                RainIntensity.MODERATE,
                capturedEvent.getPeakIntensity()
        );

        assertEquals(
                reading.getDeviceTimestamp(),
                capturedEvent.getStartTime()
        );

        ArgumentCaptor<LiveWeatherUpdate> updateCaptor =
                ArgumentCaptor.forClass(
                        LiveWeatherUpdate.class
                );

        verify(liveWeatherUpdateService)
                .publish(
                        eq("WS-001"),
                        updateCaptor.capture()
                );

        LiveWeatherUpdate publishedUpdate =
                updateCaptor.getValue();

        assertSame(
                assessmentResponse,
                publishedUpdate.getAssessment()
        );

        assertSame(
                eventResponse,
                publishedUpdate.getCurrentEvent()
        );

        assertNull(
                publishedUpdate.getEnvironment()
        );
    }

    @Test
    void confirmedRainShouldUpdateExistingEvent() {

        SensorReading reading = new SensorReading();
        reading.setDeviceId("WS-001");
        reading.setDeviceTimestamp(
                LocalDateTime.of(2026, 9, 11, 20, 5)
        );

        EventAssessment assessment = new EventAssessment();

        assessment.setEventClassification(
                EventClassification.RAIN_CONFIRMED
        );
        assessment.setActiveZoneCount(4);
        assessment.setSpatialCoverage(1.0);
        assessment.setPersistentPiezoActivity(true);

        RainEvent existingEvent = new RainEvent();
        existingEvent.setId(10L);
        existingEvent.setDeviceId("WS-001");
        existingEvent.setStartTime(
                LocalDateTime.of(2026, 9, 11, 20, 0)
        );
        existingEvent.setStatus(RainEventStatus.ACTIVE);

        when(piezoAnalyzer.analyzeZones(any(), any()))
                .thenReturn(new boolean[]{true, true, true, true});

        when(crossZoneAnalyzer.countActiveZones(any()))
                .thenReturn(4);

        when(crossZoneAnalyzer.calculateSpatialCoverage(any()))
                .thenReturn(1.0);

        when(environmentalReadingRepository
                .findTopByDeviceIdOrderByDeviceTimestampDesc(anyString()))
                .thenReturn(Optional.empty());

        when(evidenceFusion.fuse(any(), any()))
                .thenReturn(assessment);

        when(rainEventRepository
                .findTopByDeviceIdAndStatusInOrderByStartTimeDesc(
                        eq("WS-001"), anyList()))
                .thenReturn(Optional.of(existingEvent));

        when(intensityClassifier.calculateIntensityScore(
                any(), eq(1.0), any()))
                .thenReturn(0.8);

        when(intensityClassifier.classify(
                eq(0.8), any()))
                .thenReturn(RainIntensity.HEAVY);

        when(rainEventRepository.save(any(RainEvent.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        rainEventService.processReading(
                reading,
                configuration()
        );

        assertEquals(10L, existingEvent.getId());
        assertEquals(RainEventStatus.ACTIVE, existingEvent.getStatus());
        assertEquals(RainIntensity.HEAVY, existingEvent.getPeakIntensity());

        verify(rainEventRepository).save(existingEvent);
    }

    @Test
    void candidateWithoutExistingEventShouldNotCreateEvent() {

        SensorReading reading = new SensorReading();
        reading.setDeviceId("WS-001");
        reading.setDeviceTimestamp(LocalDateTime.now());

        EventAssessment assessment = new EventAssessment();
        assessment.setEventClassification(
                EventClassification.RAIN_CANDIDATE
        );
        assessment.setActiveZoneCount(1);
        assessment.setSpatialCoverage(0.25);

        when(piezoAnalyzer.analyzeZones(any(), any()))
                .thenReturn(new boolean[]{true, false, false, false});

        when(crossZoneAnalyzer.countActiveZones(any()))
                .thenReturn(1);

        when(crossZoneAnalyzer.calculateSpatialCoverage(any()))
                .thenReturn(0.25);

        when(environmentalReadingRepository
                .findTopByDeviceIdOrderByDeviceTimestampDesc(anyString()))
                .thenReturn(Optional.empty());

        when(evidenceFusion.fuse(any(), any()))
                .thenReturn(assessment);

        when(rainEventRepository
                .findTopByDeviceIdAndStatusInOrderByStartTimeDesc(
                        anyString(), anyList()))
                .thenReturn(Optional.empty());

        rainEventService.processReading(
                reading,
                configuration()
        );

        verify(rainEventRepository, never())
                .save(any(RainEvent.class));
    }

    @Test
    void noActivityShouldMoveActiveEventToEnding() {

        SensorReading reading = new SensorReading();
        reading.setDeviceId("WS-001");
        reading.setDeviceTimestamp(
                LocalDateTime.of(2026, 9, 11, 20, 10)
        );

        EventAssessment assessment = new EventAssessment();
        assessment.setEventClassification(
                EventClassification.NO_ACTIVITY
        );
        assessment.setActiveZoneCount(0);
        assessment.setSpatialCoverage(0.0);

        RainEvent event = new RainEvent();
        event.setId(10L);
        event.setDeviceId("WS-001");
        event.setStartTime(
                LocalDateTime.of(2026, 9, 11, 20, 0)
        );
        event.setStatus(RainEventStatus.ACTIVE);

        when(piezoAnalyzer.analyzeZones(any(), any()))
                .thenReturn(new boolean[]{false, false, false, false});

        when(crossZoneAnalyzer.countActiveZones(any()))
                .thenReturn(0);

        when(crossZoneAnalyzer.calculateSpatialCoverage(any()))
                .thenReturn(0.0);

        when(environmentalReadingRepository
                .findTopByDeviceIdOrderByDeviceTimestampDesc(anyString()))
                .thenReturn(Optional.empty());

        when(evidenceFusion.fuse(any(), any()))
                .thenReturn(assessment);

        when(rainEventRepository
                .findTopByDeviceIdAndStatusInOrderByStartTimeDesc(
                        anyString(), anyList()))
                .thenReturn(Optional.of(event));

        when(rainEventRepository.save(any(RainEvent.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        rainEventService.processReading(
                reading,
                configuration()
        );

        assertEquals(
                RainEventStatus.ENDING,
                event.getStatus()
        );

        verify(rainEventRepository).save(event);
    }

    @Test
    void rainReturningDuringEndingShouldReactivateSameEvent() {

        Configuration configuration = configuration();

        SensorReading reading = new SensorReading();
        reading.setDeviceId("WS-001");
        reading.setDeviceTimestamp(
                LocalDateTime.of(2026, 9, 11, 20, 20)
        );

        EventAssessment assessment = new EventAssessment();

        assessment.setEventClassification(
                EventClassification.RAIN_CANDIDATE
        );
        assessment.setActiveZoneCount(2);
        assessment.setSpatialCoverage(0.5);
        assessment.setPersistentPiezoActivity(false);

        RainEvent event = new RainEvent();

        event.setId(10L);
        event.setDeviceId("WS-001");
        event.setStartTime(
                LocalDateTime.of(2026, 9, 11, 20, 0)
        );
        event.setStatus(RainEventStatus.ENDING);
        event.setEndTime(null);

        when(piezoAnalyzer.analyzeZones(any(), any()))
                .thenReturn(new boolean[]{true, true, false, false});

        when(crossZoneAnalyzer.countActiveZones(any()))
                .thenReturn(2);

        when(crossZoneAnalyzer.calculateSpatialCoverage(any()))
                .thenReturn(0.5);

        when(environmentalReadingRepository
                .findTopByDeviceIdOrderByDeviceTimestampDesc(anyString()))
                .thenReturn(Optional.empty());

        when(evidenceFusion.fuse(any(), any()))
                .thenReturn(assessment);

        when(rainEventRepository
                .findTopByDeviceIdAndStatusInOrderByStartTimeDesc(
                        anyString(), anyList()))
                .thenReturn(Optional.of(event));

        when(rainEventRepository.save(any(RainEvent.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RainEventService service = rainEventService;

        EventAssessment result =
                service.processReading(
                        reading,
                        configuration
                );

        assertEquals(
                EventClassification.RAIN_CANDIDATE,
                result.getEventClassification()
        );

        assertEquals(
                10L,
                event.getId()
        );

        assertEquals(
                RainEventStatus.ACTIVE,
                event.getStatus()
        );

        assertNull(event.getEndTime());

        assertNull(
                event.getPeakIntensity()
        );

        verify(rainEventRepository).save(event);
    }
}
