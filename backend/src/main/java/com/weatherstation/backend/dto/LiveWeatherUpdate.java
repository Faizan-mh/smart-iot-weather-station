package com.weatherstation.backend.dto;

import com.weatherstation.backend.processing.EventAssessment;

public class LiveWeatherUpdate {
    private EventAssessmentResponse assessment;
    private CurrentRainEventResponse currentEvent;
    private EnvironmentalReadingResponse environment;

    public EventAssessmentResponse getAssessment() {
        return assessment;
    }

    public void setAssessment(EventAssessmentResponse assessment) {
        this.assessment = assessment;
    }

    public CurrentRainEventResponse getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(CurrentRainEventResponse currentEvent) {
        this.currentEvent = currentEvent;
    }

    public EnvironmentalReadingResponse getEnvironment() {
        return environment;
    }

    public void setEnvironment(EnvironmentalReadingResponse environment) {
        this.environment = environment;
    }
}
