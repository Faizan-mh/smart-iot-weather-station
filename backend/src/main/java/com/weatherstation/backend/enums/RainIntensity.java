package com.weatherstation.backend.enums;

public enum RainIntensity {
    LIGHT(1),
    MODERATE(2),
    HEAVY(3);

    private final int severity;
    RainIntensity(int severity) {
        this.severity = severity;
    }
    public int getSeverity() {
        return severity;
    }
}
