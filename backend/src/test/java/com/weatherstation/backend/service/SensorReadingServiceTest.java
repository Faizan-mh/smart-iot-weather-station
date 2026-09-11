package com.weatherstation.backend.service;

import com.weatherstation.backend.dto.SensorReadingRequest;
import com.weatherstation.backend.dto.SensorReadingResponse;
import com.weatherstation.backend.entity.SensorReading;
import com.weatherstation.backend.entity.Device;
import com.weatherstation.backend.exception.DuplicateResourceException;
import com.weatherstation.backend.exception.ResourceNotFoundException;
import com.weatherstation.backend.processing.Configuration;
import com.weatherstation.backend.repository.DeviceRepository;
import com.weatherstation.backend.repository.SensorReadingRepository;
import com.weatherstation.backend.mapper.SensorReadingMapper;
import com.weatherstation.backend.processing.ConfigurationProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorReadingServiceTest {

    @Mock
    private SensorReadingRepository sensorReadingRepository;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private SensorReadingMapper sensorReadingMapper;

    @Mock
    private RainEventService rainEventService;

    @Mock
    private ConfigurationProvider configurationProvider;

    @InjectMocks
    private SensorReadingService sensorReadingService;


    @Test
    void shouldCreateSensorReadingSuccessfully() {

        SensorReadingRequest request = new SensorReadingRequest();

        SensorReading sensorReading = new SensorReading();
        sensorReading.setDeviceId("WS-001");
        sensorReading.setMessageId("msg-001");

        SensorReadingResponse response = new SensorReadingResponse();

        Configuration configuration = new Configuration();

        when(sensorReadingMapper.toEntity(request))
                .thenReturn(sensorReading);

        when(deviceRepository.findByDeviceId("WS-001"))
                .thenReturn(new Device());

        when(sensorReadingRepository.existsByDeviceIdAndMessageId(
                "WS-001",
                "msg-001"))
                .thenReturn(false);

        when(sensorReadingRepository.save(sensorReading))
                .thenReturn(sensorReading);

        when(configurationProvider.getConfiguration())
                .thenReturn(configuration);

        when(sensorReadingMapper.toDto(sensorReading))
                .thenReturn(response);

        SensorReadingResponse result =
                sensorReadingService.createSensorReading(request);


        assertNotNull(result);
        assertSame(response, result);

        verify(sensorReadingMapper).toEntity(request);

        verify(deviceRepository)
                .findByDeviceId("WS-001");

        verify(sensorReadingRepository)
                .existsByDeviceIdAndMessageId(
                        "WS-001",
                        "msg-001");

        verify(sensorReadingRepository)
                .save(sensorReading);

        verify(configurationProvider)
                .getConfiguration();

        verify(rainEventService)
                .processReading(sensorReading, configuration);

        verify(sensorReadingMapper)
                .toDto(sensorReading);
    }


    @Test
    void shouldThrowExceptionWhenDeviceDoesNotExist() {

        SensorReadingRequest request = new SensorReadingRequest();

        SensorReading sensorReading = new SensorReading();
        sensorReading.setDeviceId("UNKNOWN");
        sensorReading.setMessageId("msg-001");

        when(sensorReadingMapper.toEntity(request))
                .thenReturn(sensorReading);

        when(deviceRepository.findByDeviceId("UNKNOWN"))
                .thenReturn(null);

        assertThrows(
                ResourceNotFoundException.class,
                () -> sensorReadingService.createSensorReading(request)
        );

        verify(deviceRepository)
                .findByDeviceId("UNKNOWN");

        verify(sensorReadingRepository, never())
                .save(any(SensorReading.class));

        verify(configurationProvider, never())
                .getConfiguration();

        verify(rainEventService, never())
                .processReading(any(), any());
    }


    @Test
    void shouldThrowExceptionWhenSensorReadingAlreadyExists() {

        SensorReadingRequest request = new SensorReadingRequest();

        SensorReading sensorReading = new SensorReading();
        sensorReading.setDeviceId("WS-001");
        sensorReading.setMessageId("msg-001");

        when(sensorReadingMapper.toEntity(request))
                .thenReturn(sensorReading);

        when(deviceRepository.findByDeviceId("WS-001"))
                .thenReturn(new Device());

        when(sensorReadingRepository.existsByDeviceIdAndMessageId(
                "WS-001",
                "msg-001"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> sensorReadingService.createSensorReading(request)
        );

        verify(deviceRepository)
                .findByDeviceId("WS-001");

        verify(sensorReadingRepository)
                .existsByDeviceIdAndMessageId(
                        "WS-001",
                        "msg-001");

        verify(sensorReadingRepository, never())
                .save(any(SensorReading.class));

        verify(configurationProvider, never())
                .getConfiguration();

        verify(rainEventService, never())
                .processReading(any(), any());
    }
}
