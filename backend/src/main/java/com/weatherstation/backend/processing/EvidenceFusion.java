package com.weatherstation.backend.processing;

public class EvidenceFusion {

    public EventAssessment fuse(
            EventAssessment assessment,
            Configuration configuration) {

        int activeZones = assessment.getActiveZoneCount();

        boolean spatiallyStrong =
                activeZones >= configuration.getMinimumActiveZones();

        boolean persistent =
                assessment.isPersistentPiezoActivity();

        boolean rainSensorWet =
                assessment.isRainSensorWet();

        boolean piezoActivity =
                activeZones > 0;

        boolean highWind =
                assessment.getWindSpeedKmh() != null
                        && assessment.getWindSpeedKmh()
                        >= configuration.getHighWindThresholdKmh();

        if (persistent
                && spatiallyStrong
                && rainSensorWet) {

            assessment.setEventClassification(
                    EventClassification.RAIN_CONFIRMED);

            assessment.setReasoning(
                    buildConfirmedReasoning(
                            activeZones,
                            highWind));

            return assessment;
        }

        if (!piezoActivity && !rainSensorWet) {

            assessment.setEventClassification(
                    EventClassification.NO_ACTIVITY);

            assessment.setReasoning(
                    "No significant piezo activity or wetness "
                            + "detected.");

            return assessment;
        }

        assessment.setEventClassification(
                EventClassification.RAIN_CANDIDATE);

        assessment.setReasoning(
                buildCandidateReasoning(
                        assessment,
                        spatiallyStrong,
                        persistent,
                        highWind));

        return assessment;
    }

    private String buildConfirmedReasoning(
            int activeZones,
            boolean highWind) {

        StringBuilder reason = new StringBuilder();

        reason.append(
                        "Persistent piezo activity detected across ")
                .append(activeZones)
                .append(" active zones and corroborated "
                        + "by the wetness sensor.");

        if (highWind) {
            reason.append(
                    " High wind is present and has been "
                            + "retained as environmental context, "
                            + "but does not override the "
                            + "multi-sensor agreement.");
        }

        return reason.toString();
    }

    private String buildCandidateReasoning(
            EventAssessment assessment,
            boolean spatiallyStrong,
            boolean persistent,
            boolean highWind) {

        boolean rainSensorWet =
                assessment.isRainSensorWet();

        int activeZones =
                assessment.getActiveZoneCount();

        StringBuilder reason =
                new StringBuilder();

        if (activeZones > 0 && persistent) {

            if (spatiallyStrong) {
                reason.append(
                        "Persistent multi-zone piezo activity "
                                + "detected, but confirmation is "
                                + "incomplete.");
            } else {
                reason.append(
                        "Persistent but localized piezo "
                                + "activity detected; spatial "
                                + "coverage is insufficient for "
                                + "confirmation.");
            }

        } else if (activeZones > 0) {

            reason.append(
                    "Piezo activity detected, but it has "
                            + "not persisted across the required "
                            + "number of windows.");

        }

        if (rainSensorWet) {

            if (reason.length() > 0) {
                reason.append(" ");
            }

            reason.append(
                    "The wetness sensor indicates a wet "
                            + "surface, but corresponding piezo "
                            + "evidence is insufficient for "
                            + "confirmation.");
        }

        if (persistent && spatiallyStrong
                && !rainSensorWet) {

            if (reason.length() > 0) {
                reason.append(" ");
            }

            reason.append(
                    "Piezo and wetness evidence are "
                            + "currently inconsistent.");
        }
        if (highWind) {

            if (reason.length() > 0) {
                reason.append(" ");
            }

            reason.append(
                    "High wind is present and may contribute "
                            + "to mechanical piezo disturbances.");
        }
        if (reason.length() == 0) {
            reason.append(
                    "Some evidence of activity was detected, "
                            + "but it is insufficient for rainfall "
                            + "confirmation.");
        }

        return reason.toString();
    }
}