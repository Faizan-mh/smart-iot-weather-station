package com.weatherstation.backend.processing;

import com.weatherstation.backend.entity.SensorReading;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PiezoAnalyzerTest {

    @Test
    void DetectActiveZoneWhenTwoInputAreSignificant(){
        PiezoAnalyzer piezoAnalyzer = new PiezoAnalyzer();
        boolean result = piezoAnalyzer.isZoneActive(
                20.0,
                8.0,
                2,
                10.0,
                5.0,
                5
        );
        assertTrue(result);
    }

    @Test
    void ShouldNotDetectWhenOneInputIsSignificant(){
        PiezoAnalyzer piezoAnalyzer = new PiezoAnalyzer();
        boolean result = piezoAnalyzer.isZoneActive(
                20.0,
                2.0,
                2,
                10.0,
                5.0,
                5
        );
        assertFalse(result);
    }

    @Test
    void shouldAnalyzeAllFourZones() {

        PiezoAnalyzer analyzer = new PiezoAnalyzer();
        PiezoConfiguration configuration = new PiezoConfiguration();

        configuration.setP1PeakThreshold(10);
        configuration.setP1RmsThreshold(5);
        configuration.setP1ImpactThreshold(5);

        configuration.setP2PeakThreshold(10);
        configuration.setP2RmsThreshold(5);
        configuration.setP2ImpactThreshold(5);

        configuration.setP3PeakThreshold(10);
        configuration.setP3RmsThreshold(5);
        configuration.setP3ImpactThreshold(5);

        configuration.setP4PeakThreshold(10);
        configuration.setP4RmsThreshold(5);
        configuration.setP4ImpactThreshold(5);

        SensorReading reading = new SensorReading();

        reading.setP1Peak(20.0);
        reading.setP1Rms(8.0);
        reading.setP1ImpactCount(8);

        reading.setP2Peak(20.0);
        reading.setP2Rms(2.0);
        reading.setP2ImpactCount(2);

        reading.setP3Peak(20.0);
        reading.setP3Rms(8.0);
        reading.setP3ImpactCount(8);

        reading.setP4Peak(2.0);
        reading.setP4Rms(2.0);
        reading.setP4ImpactCount(2);

        boolean[] result = analyzer.analyzeZones(reading, configuration);

        assertTrue(result[0]);
        assertFalse(result[1]);
        assertTrue(result[2]);
        assertFalse(result[3]);
    }
}
