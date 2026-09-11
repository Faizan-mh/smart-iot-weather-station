package com.weatherstation.backend.processing;

import com.weatherstation.backend.entity.SensorReading;
import com.weatherstation.backend.enums.RainIntensity;
import org.springframework.stereotype.Component;

@Component
public class IntensityClassifier {
    public double calculateImpactFrequency(SensorReading reading) {
        double windowDurationSeconds = reading.getWindowDurationMs() / 1000.0;
        if (windowDurationSeconds <= 0) {
            throw new IllegalArgumentException("Window duration must be greater than zero");
        }
        int totalImpacts = reading.getP1ImpactCount()+
                reading.getP2ImpactCount()+
                reading.getP3ImpactCount()+
                reading.getP4ImpactCount();
        return totalImpacts / windowDurationSeconds;
    }
    public double calculateAverageRms(SensorReading reading) {
        return (reading.getP1Rms()
                + reading.getP2Rms()
                + reading.getP3Rms()
                + reading.getP4Rms()
                ) / 4.0;
    }
    public double normalize(double value,double reference) {
        if (reference <= 0) {
            throw new IllegalArgumentException("Normalization Reference must be greater than zero");
        }
        return Math.min(value/reference, 1.0);
    }
    public double calculateIntensityScore(SensorReading reading,double spatialCoverage, Configuration configuration){
        double impactFrequency = calculateImpactFrequency(reading);
        double averageRms = calculateAverageRms(reading);
        double normalizedImpact = normalize(impactFrequency,configuration.getImpactFrequencyReference());
        double normalizedRms = normalize(averageRms,configuration.getRmsReference());
        double normalizedSpatialCoverage = spatialCoverage;
        return (0.5*normalizedImpact)+
                (0.3*normalizedRms)+
                (0.2* normalizedSpatialCoverage);
    }
    public RainIntensity classify(double intensityScore, Configuration configuration){
        if(intensityScore >= configuration.getHeavyIntensityThreshold()){
            return RainIntensity.HEAVY;
        }
        if(intensityScore>= configuration.getModerateIntensityThreshold()){
            return RainIntensity.MODERATE;
        }
        return RainIntensity.LIGHT;
    }
}
