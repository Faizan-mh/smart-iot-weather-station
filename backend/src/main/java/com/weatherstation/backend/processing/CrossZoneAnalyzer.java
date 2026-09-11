package com.weatherstation.backend.processing;

import org.springframework.stereotype.Component;

@Component
public class CrossZoneAnalyzer {
    public int countActiveZones(boolean[] activeZones){
        int activeCount = 0;
        for(boolean active: activeZones){
            if(active){
                activeCount++;
            }
        }
        return activeCount;
    }

    public double calculateSpatialCoverage(boolean[] activeZones){
        return (double) countActiveZones(activeZones) / activeZones.length;
    }
}
