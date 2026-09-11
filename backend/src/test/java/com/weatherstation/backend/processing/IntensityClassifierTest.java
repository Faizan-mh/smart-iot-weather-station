package com.weatherstation.backend.processing;

import com.weatherstation.backend.entity.SensorReading;
import org.junit.jupiter.api.Test;
import com.weatherstation.backend.enums.RainIntensity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntensityClassifierTest {
    IntensityClassifier classifier = new IntensityClassifier();
    @Test
    void shouldCalculateAverageRms() {
        SensorReading reading = new SensorReading();

        reading.setP1Rms(40.0);
        reading.setP2Rms(35.0);
        reading.setP3Rms(30.0);
        reading.setP4Rms(0.0);

        double result = classifier.calculateAverageRms(reading);

        assertEquals(26.25, result, 0.001);
    }
@Test
    void shouldCalculateImpactFrequency() {
        SensorReading reading = new SensorReading();

        reading.setP1ImpactCount(5);
        reading.setP2ImpactCount(4);
        reading.setP3ImpactCount(3);
        reading.setP4ImpactCount(0);
        reading.setWindowDurationMs(2000);

        double result = classifier.calculateImpactFrequency(reading);

        assertEquals(6.0, result, 0.001);
    }
@Test
    void shouldCalculateIntensityScore() {
        SensorReading reading = new SensorReading();

        reading.setP1ImpactCount(3);
        reading.setP2ImpactCount(3);
        reading.setP3ImpactCount(3);
        reading.setP4ImpactCount(3);
        reading.setWindowDurationMs(2000);

        reading.setP1Rms(25.0);
        reading.setP2Rms(25.0);
        reading.setP3Rms(25.0);
        reading.setP4Rms(25.0);

        Configuration configuration = new Configuration();
        configuration.setImpactFrequencyReference(10.0);
        configuration.setRmsReference(50.0);

        double result =
                classifier.calculateIntensityScore(
                        reading,
                        3.0 / 4.0,
                        configuration
                );

        assertEquals(0.60, result, 0.001);
    }

    @Test
    void shouldClassifyLightRain() {
        Configuration configuration = new Configuration();
        configuration.setModerateIntensityThreshold(0.40);
        configuration.setHeavyIntensityThreshold(0.70);

        assertEquals(
                RainIntensity.LIGHT,
                classifier.classify(0.20, configuration)
        );
    }

    @Test
    void shouldClassifyModerateRain() {
        Configuration configuration = new Configuration();
        configuration.setModerateIntensityThreshold(0.40);
        configuration.setHeavyIntensityThreshold(0.70);

        assertEquals(
                RainIntensity.MODERATE,
                classifier.classify(0.50, configuration)
        );
    }
    @Test
    void shouldClassifyHeavyRain() {
        Configuration configuration = new Configuration();
        configuration.setModerateIntensityThreshold(0.40);
        configuration.setHeavyIntensityThreshold(0.70);

        assertEquals(
                RainIntensity.HEAVY,
                classifier.classify(0.80, configuration)
        );
    }
}
