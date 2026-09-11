package com.weatherstation.backend.processing;

import org.springframework.stereotype.Component;

@Component
public class TemporalAnalyzer {
    private int consecutiveActiveWindows = 0;
    public boolean update(
            int activeZoneCount,
            int minimumActiveZones,
            int requiredConsecutiveWindows
    ){
        if(activeZoneCount >= minimumActiveZones){
            consecutiveActiveWindows++;
        }else {
            consecutiveActiveWindows = 0;
        }
        return consecutiveActiveWindows >= requiredConsecutiveWindows;
    }
}
