package com.weatherstation.backend.processing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CrossZoneAnalyzerTest {

    @Test
    void CountActiveZones() {
        CrossZoneAnalyzer analyzer = new CrossZoneAnalyzer();
        boolean[] activeZones = {true, false, true, true};
        int result = analyzer.countActiveZones(activeZones);
        assertEquals(3, result);
    }
    @Test
    void ZeroActiveZones() {
        CrossZoneAnalyzer analyzer = new CrossZoneAnalyzer();
        boolean[] activeZones = {false, false, false, false};
        assertEquals(0, analyzer.countActiveZones(activeZones));
    }
    @Test
    void calculateSpatialCoverage() {
        CrossZoneAnalyzer analyzer = new CrossZoneAnalyzer();
        boolean[] activeZones = {true, false, false, true};
        assertEquals(0.5,analyzer.calculateSpatialCoverage(activeZones));
    }
}
