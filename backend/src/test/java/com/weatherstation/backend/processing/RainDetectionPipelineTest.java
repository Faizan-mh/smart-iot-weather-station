package com.weatherstation.backend.processing;

import com.weatherstation.backend.entity.SensorReading;
import com.weatherstation.backend.entity.EnvironmentalReading;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class RainDetectionPipelineTest {

    @Test
    void shouldProcessMultipleWindowsThroughRainDetectionPipeline() {

        Configuration configuration = new Configuration();

        configuration.setP1PeakThreshold(100);
        configuration.setP2PeakThreshold(100);
        configuration.setP3PeakThreshold(100);
        configuration.setP4PeakThreshold(100);

        configuration.setP1RmsThreshold(30);
        configuration.setP2RmsThreshold(30);
        configuration.setP3RmsThreshold(30);
        configuration.setP4RmsThreshold(30);

        configuration.setP1ImpactThreshold(3);
        configuration.setP2ImpactThreshold(3);
        configuration.setP3ImpactThreshold(3);
        configuration.setP4ImpactThreshold(3);

        configuration.setMinimumActiveZones(2);
        configuration.setRequiredConsecutiveWindows(3);

        configuration.setRainSensorWetThreshold(500);
        configuration.setHighWindThresholdKmh(20);

        PiezoAnalyzer piezoAnalyzer = new PiezoAnalyzer();
        CrossZoneAnalyzer crossZoneAnalyzer = new CrossZoneAnalyzer();
        TemporalAnalyzer temporalAnalyzer = new TemporalAnalyzer();
        EvidenceFusion evidenceFusion = new EvidenceFusion();

        SensorReading window1 =
                createSensorReading(
                        100,
                        100,
                        100,
                        100,
                        20,
                        20,
                        20,
                        20,
                        1,
                        1,
                        1,
                        1,
                        300
                );

        SensorReading window2 =
                createSensorReading(
                        150,
                        160,
                        80,
                        70,
                        40,
                        45,
                        20,
                        15,
                        5,
                        6,
                        1,
                        1,
                        300
                );

        SensorReading window3 =
                createSensorReading(
                        180,
                        170,
                        160,
                        70,
                        50,
                        48,
                        45,
                        15,
                        6,
                        7,
                        5,
                        1,
                        300
                );

        SensorReading window4 =
                createSensorReading(
                        200,
                        190,
                        180,
                        170,
                        60,
                        55,
                        50,
                        48,
                        8,
                        7,
                        6,
                        7,
                        700
                );


        EnvironmentalReading environment =
                new EnvironmentalReading();

        environment.setTemperatureC(27.5);
        environment.setHumidityPercent(82.0);
        environment.setPressureHpa(1007.5);
        environment.setWindSpeedKmh(12.0);
        environment.setWindGustKmh(18.0);


        processWindow(
                1,
                window1,
                environment,
                configuration,
                piezoAnalyzer,
                crossZoneAnalyzer,
                temporalAnalyzer,
                evidenceFusion
        );

        processWindow(
                2,
                window2,
                environment,
                configuration,
                piezoAnalyzer,
                crossZoneAnalyzer,
                temporalAnalyzer,
                evidenceFusion
        );

        processWindow(
                3,
                window3,
                environment,
                configuration,
                piezoAnalyzer,
                crossZoneAnalyzer,
                temporalAnalyzer,
                evidenceFusion
        );

        processWindow(
                4,
                window4,
                environment,
                configuration,
                piezoAnalyzer,
                crossZoneAnalyzer,
                temporalAnalyzer,
                evidenceFusion
        );
    }


    private void processWindow(
            int windowNumber,
            SensorReading reading,
            EnvironmentalReading environment,
            Configuration configuration,
            PiezoAnalyzer piezoAnalyzer,
            CrossZoneAnalyzer crossZoneAnalyzer,
            TemporalAnalyzer temporalAnalyzer,
            EvidenceFusion evidenceFusion
    ) {

        boolean[] activeZones =
                piezoAnalyzer.analyzeZones(
                        reading,
                        configuration
                );

        int activeZoneCount =
                crossZoneAnalyzer.countActiveZones(
                        activeZones
                );

        double spatialCoverage =
                crossZoneAnalyzer.calculateSpatialCoverage(
                        activeZones
                );

        boolean persistent =
                temporalAnalyzer.update(
                        activeZoneCount,
                        configuration.getMinimumActiveZones(),
                        configuration.getRequiredConsecutiveWindows()
                );


        EventAssessment assessment =
                new EventAssessment();

        assessment.setActiveZoneCount(activeZoneCount);
        assessment.setSpatialCoverage(spatialCoverage);
        assessment.setPersistentPiezoActivity(persistent);

        assessment.setRainSensorWet(
                reading.getRainSensor()
                        >= configuration.getRainSensorWetThreshold()
        );

        assessment.setTemperatureC(
                environment.getTemperatureC()
        );

        assessment.setHumidityPercent(
                environment.getHumidityPercent()
        );

        assessment.setPressureHpa(
                environment.getPressureHpa()
        );

        assessment.setWindSpeedKmh(
                environment.getWindSpeedKmh()
        );

        assessment.setWindGustKmh(
                environment.getWindGustKmh()
        );


        EventAssessment result =
                evidenceFusion.fuse(
                        assessment,
                        configuration
                );


        System.out.println();
        System.out.println("========== WINDOW "
                + windowNumber
                + " ==========");

        System.out.println(
                "Active zones: "
                        + activeZoneCount
                        + "/4"
        );

        System.out.println(
                "Spatial coverage: "
                        + spatialCoverage
        );

        System.out.println(
                "Persistent piezo activity: "
                        + persistent
        );

        System.out.println(
                "Rain sensor wet: "
                        + result.isRainSensorWet()
        );

        System.out.println(
                "Wind speed: "
                        + result.getWindSpeedKmh()
                        + " km/h"
        );

        System.out.println(
                "Classification: "
                        + result.getEventClassification()
        );

        System.out.println(
                "Reasoning: "
                        + result.getReasoning()
        );
    }


    private SensorReading createSensorReading(
            double p1Peak,
            double p2Peak,
            double p3Peak,
            double p4Peak,

            double p1Rms,
            double p2Rms,
            double p3Rms,
            double p4Rms,

            int p1Impact,
            int p2Impact,
            int p3Impact,
            int p4Impact,

            double rainSensor
    ) {

        SensorReading reading =
                new SensorReading();

        reading.setDeviceId("TEST-DEVICE");

        reading.setDeviceTimestamp(
                LocalDateTime.now()
        );

        reading.setServerTimestamp(
                LocalDateTime.now()
        );

        reading.setMessageId(
                "TEST-" + System.nanoTime()
        );

        reading.setP1Peak(p1Peak);
        reading.setP2Peak(p2Peak);
        reading.setP3Peak(p3Peak);
        reading.setP4Peak(p4Peak);

        reading.setP1Rms(p1Rms);
        reading.setP2Rms(p2Rms);
        reading.setP3Rms(p3Rms);
        reading.setP4Rms(p4Rms);

        reading.setP1ImpactCount(p1Impact);
        reading.setP2ImpactCount(p2Impact);
        reading.setP3ImpactCount(p3Impact);
        reading.setP4ImpactCount(p4Impact);

        reading.setRainSensor(rainSensor);

        return reading;
    }
}
