package com.weatherstation.backend.mapper;

import com.weatherstation.backend.dto.DeviceRequest;
import com.weatherstation.backend.dto.DeviceResponse;
import com.weatherstation.backend.dto.SensorReadingRequest;
import com.weatherstation.backend.dto.SensorReadingResponse;
import com.weatherstation.backend.entity.Device;
import com.weatherstation.backend.entity.SensorReading;
import org.springframework.stereotype.Component;

@Component
public class SensorReadingMapper {
    public SensorReading toEntity(SensorReadingRequest request){
        SensorReading sensorReading = new SensorReading();

        sensorReading.setDeviceId(request.getDeviceId());
        sensorReading.setMessageId(request.getMessageId());
        sensorReading.setDeviceTimestamp(request.getDeviceTimestamp());
        sensorReading.setRainSensor(request.getRainSensor());
        sensorReading.setP1Peak(request.getP1Peak());
        sensorReading.setP2Peak(request.getP2Peak());
        sensorReading.setP3Peak(request.getP3Peak());
        sensorReading.setP4Peak(request.getP4Peak());

        sensorReading.setP1Rms(request.getP1Rms());
        sensorReading.setP2Rms(request.getP2Rms());
        sensorReading.setP3Rms(request.getP3Rms());
        sensorReading.setP4Rms(request.getP4Rms());

        sensorReading.setP1ImpactCount(request.getP1ImpactCount());
        sensorReading.setP2ImpactCount(request.getP2ImpactCount());
        sensorReading.setP3ImpactCount(request.getP3ImpactCount());
        sensorReading.setP4ImpactCount(request.getP4ImpactCount());

        sensorReading.setSamplingRateHz(request.getSamplingRateHz());
        sensorReading.setWindowDurationMs(request.getWindowDurationMs());

        return sensorReading;
    }

    public SensorReadingResponse toDto(SensorReading response){
        SensorReadingResponse sensorReadingResponse = new SensorReadingResponse();

        sensorReadingResponse.setId(response.getId());
        sensorReadingResponse.setDeviceId(response.getDeviceId());
        sensorReadingResponse.setMessageId(response.getMessageId());
        sensorReadingResponse.setDeviceTimestamp(response.getDeviceTimestamp());
        sensorReadingResponse.setServerTimeStamp(response.getServerTimestamp());
    return sensorReadingResponse;
}
}
