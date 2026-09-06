package com.weatherstation.backend.controller;

import com.weatherstation.backend.dto.SensorReadingRequest;
import com.weatherstation.backend.dto.SensorReadingResponse;
import com.weatherstation.backend.service.SensorReadingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/sensor")
public class SensorReadingController {

    SensorReadingService sensorReadingService;

    public SensorReadingController(SensorReadingService sensorReadingService) {
        this.sensorReadingService = sensorReadingService;
    }

    @PostMapping
    public ResponseEntity<SensorReadingResponse> createSensorReading(@RequestBody SensorReadingRequest request){
        SensorReadingResponse response = sensorReadingService.createSensorReading(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
