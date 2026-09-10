package com.weatherstation.backend.processing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EvidenceFusionTest {

    private Configuration createConfiguration() {
        Configuration configuration = new Configuration();

        configuration.setMinimumActiveZones(2);
        configuration.setHighWindThresholdKmh(20.0);

        return configuration;
    }

    private EventAssessment createAssessment(
            int activeZones,
            boolean persistent,
            boolean rainSensorWet) {

        EventAssessment assessment = new EventAssessment();

        assessment.setActiveZoneCount(activeZones);
        assessment.setPersistentPiezoActivity(persistent);
        assessment.setRainSensorWet(rainSensorWet);

        return assessment;
    }


    @Test
    void NoActivity() {

        Configuration configuration = createConfiguration();

        EventAssessment assessment =
                createAssessment(0, false, false);

        EvidenceFusion fusion = new EvidenceFusion();

        EventAssessment result =
                fusion.fuse(assessment, configuration);

        assertEquals(
                EventClassification.NO_ACTIVITY,
                result.getEventClassification()
        );

        assertTrue(
                result.getReasoning().contains("No significant")
        );
    }


    @Test
    void shouldReturnCandidateForLocalizedPiezoActivity() {

        Configuration configuration = createConfiguration();

        EventAssessment assessment =
                createAssessment(1, false, false);

        EvidenceFusion fusion = new EvidenceFusion();

        EventAssessment result =
                fusion.fuse(assessment, configuration);

        assertEquals(
                EventClassification.RAIN_CANDIDATE,
                result.getEventClassification()
        );

        assertTrue(
                result.getReasoning().contains("Piezo activity")
        );
    }


    @Test
    void shouldReturnCandidateForPersistentButLocalizedActivity() {

        Configuration configuration = createConfiguration();

        EventAssessment assessment =
                createAssessment(1, true, false);

        EvidenceFusion fusion = new EvidenceFusion();

        EventAssessment result =
                fusion.fuse(assessment, configuration);

        assertEquals(
                EventClassification.RAIN_CANDIDATE,
                result.getEventClassification()
        );

        assertTrue(
                result.getReasoning().contains("localized")
        );
    }


    @Test
    void shouldReturnCandidateWhenStrongPiezoHasNoWetnessConfirmation() {

        Configuration configuration = createConfiguration();

        EventAssessment assessment =
                createAssessment(4, true, false);

        EvidenceFusion fusion = new EvidenceFusion();

        EventAssessment result =
                fusion.fuse(assessment, configuration);

        assertEquals(
                EventClassification.RAIN_CANDIDATE,
                result.getEventClassification()
        );

        assertTrue(
                result.getReasoning().contains("inconsistent")
        );
    }


    @Test
    void shouldReturnCandidateWhenOnlyRainSensorIsWet() {

        Configuration configuration = createConfiguration();

        EventAssessment assessment =
                createAssessment(0, false, true);

        EvidenceFusion fusion = new EvidenceFusion();

        EventAssessment result =
                fusion.fuse(assessment, configuration);

        assertEquals(
                EventClassification.RAIN_CANDIDATE,
                result.getEventClassification()
        );

        assertTrue(
                result.getReasoning().contains("wetness")
        );
    }


    @Test
    void shouldConfirmRainWhenStrongPiezoAndWetnessAgree() {

        Configuration configuration = createConfiguration();

        EventAssessment assessment =
                createAssessment(4, true, true);

        EvidenceFusion fusion = new EvidenceFusion();

        EventAssessment result =
                fusion.fuse(assessment, configuration);

        assertEquals(
                EventClassification.RAIN_CONFIRMED,
                result.getEventClassification()
        );

        assertTrue(
                result.getReasoning().contains("corroborated")
        );
    }


    @Test
    void shouldConfirmRainAtMinimumRequiredActiveZones() {

        Configuration configuration = createConfiguration();

        EventAssessment assessment =
                createAssessment(2, true, true);

        EvidenceFusion fusion = new EvidenceFusion();

        EventAssessment result =
                fusion.fuse(assessment, configuration);

        assertEquals(
                EventClassification.RAIN_CONFIRMED,
                result.getEventClassification()
        );
    }


    @Test
    void highWindShouldNotCancelConfirmedRain() {

        Configuration configuration = createConfiguration();

        EventAssessment assessment =
                createAssessment(4, true, true);

        assessment.setWindSpeedKmh(30.0);

        EvidenceFusion fusion = new EvidenceFusion();

        EventAssessment result =
                fusion.fuse(assessment, configuration);

        assertEquals(
                EventClassification.RAIN_CONFIRMED,
                result.getEventClassification()
        );

        assertTrue(
                result.getReasoning().contains("High wind")
        );
    }


    @Test
    void highWindWithWeakPiezoShouldRemainCandidate() {

        Configuration configuration = createConfiguration();

        EventAssessment assessment =
                createAssessment(1, false, false);

        assessment.setWindSpeedKmh(30.0);

        EvidenceFusion fusion = new EvidenceFusion();

        EventAssessment result =
                fusion.fuse(assessment, configuration);

        assertEquals(
                EventClassification.RAIN_CANDIDATE,
                result.getEventClassification()
        );

        assertTrue(
                result.getReasoning().contains("High wind")
        );
    }
}
