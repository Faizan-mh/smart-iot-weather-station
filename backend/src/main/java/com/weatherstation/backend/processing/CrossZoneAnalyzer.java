package com.weatherstation.backend.processing;

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
