package com.weatherstation.backend.controller;

import com.weatherstation.backend.dto.EnvironmentalReadingRequest;
import com.weatherstation.backend.dto.EnvironmentalReadingResponse;
import com.weatherstation.backend.service.EnvironmentalReadingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/environmental-readings")
public class EnvironmentalReadingController {

    private final EnvironmentalReadingService service;

    public EnvironmentalReadingController(
            EnvironmentalReadingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EnvironmentalReadingResponse> createReading(
            @Valid @RequestBody EnvironmentalReadingRequest request) {

        EnvironmentalReadingResponse response =
                service.createReading(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping("/latest/{deviceId}")
    public ResponseEntity<EnvironmentalReadingResponse> getLatestReading(
            @PathVariable String deviceId) {

        return ResponseEntity.ok(
                service.getLatestReading(deviceId)
        );
    }
    @GetMapping("/{deviceId}")
    public ResponseEntity<List<EnvironmentalReadingResponse>> getReadingHistory(
            @PathVariable String deviceId) {

        return ResponseEntity.ok(
                service.getReadingHistory(deviceId)
        );
    }
}
