package com.weatherstation.backend.mapper;

import com.weatherstation.backend.dto.EnvironmentalReadingRequest;
import com.weatherstation.backend.entity.EnvironmentalReading;

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
    }
