package com.weatherstation.backend.mapper;

import com.weatherstation.backend.dto.EnvironmentalReadingRequest;
import com.weatherstation.backend.dto.EnvironmentalReadingResponse;
import com.weatherstation.backend.entity.EnvironmentalReading;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentalReadingMapper {

    public EnvironmentalReading toEntity(EnvironmentalReadingRequest request) {
        EnvironmentalReading environmentalReading = new EnvironmentalReading();

        environmentalReading.setDeviceId(request.getDeviceId());
        environmentalReading.setDeviceTimestamp(request.getDeviceTimestamp());
        environmentalReading.setTemperatureC(request.getTemperatureC());
        environmentalReading.setHumidityPercent(request.getHumidityPercent());
        environmentalReading.setPressureHpa(request.getPressureHpa());
        environmentalReading.setWindSpeedKmh(request.getWindSpeedKmh());
        environmentalReading.setWindGustKmh(request.getWindGustKmh());

        return environmentalReading;
    }
    public EnvironmentalReadingResponse toResponse(EnvironmentalReading environmentalReading) {
        EnvironmentalReadingResponse response = new EnvironmentalReadingResponse();
        response.setId(environmentalReading.getId());
        response.setDeviceId(environmentalReading.getDeviceId());
        response.setDeviceTimestamp(environmentalReading.getDeviceTimestamp());
        response.setTemperatureC(environmentalReading.getTemperatureC());
        response.setHumidityPercent(environmentalReading.getHumidityPercent());
        response.setPressureHpa(environmentalReading.getPressureHpa());
        response.setWindSpeedKmh(environmentalReading.getWindSpeedKmh());
        response.setWindGustKmh(environmentalReading.getWindGustKmh());
        return response;
    }
}
