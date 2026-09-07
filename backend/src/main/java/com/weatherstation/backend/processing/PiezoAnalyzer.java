package com.weatherstation.backend.processing;

import com.weatherstation.backend.entity.SensorReading;

public class PiezoAnalyzer {

    public boolean isZoneActive(
            double peak,
            double rms,
            int impactCount,
            double peakThreshold,
            double rmsThreshold,
            int impactThreshold
    ){
        int evidenceCount=0;
        if(peak>peakThreshold){
            evidenceCount++;
        }
        if(rms>rmsThreshold){
            evidenceCount++;
        }
        if(impactCount>impactThreshold){
            evidenceCount++;
        }
        return evidenceCount>=2;
    }

    public boolean[] analyzeZones(
            SensorReading reading,
            PiezoConfiguration configuration
    ){
        boolean p1Active = isZoneActive(
                reading.getP1Peak(),
                reading.getP1Rms(),
                reading.getP1ImpactCount(),
                configuration.getP1PeakThreshold(),
                configuration.getP1RmsThreshold(),
                configuration.getP1ImpactThreshold()
        );

        boolean p2Active = isZoneActive(
                reading.getP2Peak(),
                reading.getP2Rms(),
                reading.getP2ImpactCount(),
                configuration.getP2PeakThreshold(),
                configuration.getP2RmsThreshold(),
                configuration.getP2ImpactThreshold()
        );

        boolean p3Active = isZoneActive(
                reading.getP3Peak(),
                reading.getP3Rms(),
                reading.getP3ImpactCount(),
                configuration.getP3PeakThreshold(),
                configuration.getP3RmsThreshold(),
                configuration.getP3ImpactThreshold()
        );

        boolean p4Active = isZoneActive(
                reading.getP4Peak(),
                reading.getP4Rms(),
                reading.getP4ImpactCount(),
                configuration.getP4PeakThreshold(),
                configuration.getP4RmsThreshold(),
                configuration.getP4ImpactThreshold()
        );

        return new boolean[]{p1Active,p2Active,p3Active,p4Active};

    }
}
