package com.weatherstation.backend.processing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TemporalAnalyzerTest {

    @Test
    void detectPersistentActivity(){
        TemporalAnalyzer temporalAnalyzer = new TemporalAnalyzer();
        assertFalse(temporalAnalyzer.update(3,2,3));
        assertFalse(temporalAnalyzer.update(2,2,3));
        assertTrue(temporalAnalyzer.update(4,2,3));
    }

    @Test
    void shouldResetCounter(){
        TemporalAnalyzer temporalAnalyzer = new TemporalAnalyzer();
        assertFalse(temporalAnalyzer.update(3,2,3));
        assertFalse(temporalAnalyzer.update(2,2,3));
        assertFalse(temporalAnalyzer.update(1,2,3));
        assertFalse(temporalAnalyzer.update(3,2,3));
    }
}
