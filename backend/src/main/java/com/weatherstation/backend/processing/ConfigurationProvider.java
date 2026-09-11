package com.weatherstation.backend.processing;

import org.springframework.stereotype.Component;

@Component
public class ConfigurationProvider {
    private final Configuration configuration;
    public ConfigurationProvider() {
        Configuration config = new Configuration();

        config.setP1PeakThreshold(100);
        config.setP2PeakThreshold(100);
        config.setP3PeakThreshold(100);
        config.setP4PeakThreshold(100);

        config.setP1RmsThreshold(30);
        config.setP2RmsThreshold(30);
        config.setP3RmsThreshold(30);
        config.setP4RmsThreshold(30);

        config.setP1ImpactThreshold(3);
        config.setP2ImpactThreshold(3);
        config.setP3ImpactThreshold(3);
        config.setP4ImpactThreshold(3);

        // Evidence fusion
        config.setMinimumActiveZones(2);
        config.setRequiredConsecutiveWindows(3);

        // Rain sensor
        config.setRainSensorWetThreshold(500);

        // Environmental context
        config.setHighWindThresholdKmh(20);

        // Event ending
        config.setEndConfirmationSeconds(30);

        // Intensity normalization
        config.setImpactFrequencyReference(10);
        config.setRmsReference(50);

        // Intensity classification
        config.setModerateIntensityThreshold(0.40);
        config.setHeavyIntensityThreshold(0.70);

        this.configuration = config;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}