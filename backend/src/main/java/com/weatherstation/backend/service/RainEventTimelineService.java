package com.weatherstation.backend.service;

import com.weatherstation.backend.entity.RainEventTimeline;
import com.weatherstation.backend.enums.RainIntensity;
import com.weatherstation.backend.repository.RainEventTimelineRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RainEventTimelineService {
    private final RainEventTimelineRepository timelineRepository;

    public RainEventTimelineService(RainEventTimelineRepository timelineRepository) {
        this.timelineRepository = timelineRepository;
    }

    public void updateTimeline(
            Long eventId,
            LocalDateTime timestamp,
            RainIntensity newIntensity
    ){
        Optional<RainEventTimeline> latestTimeline = timelineRepository.findTopByEventIdOrderByStartTimeDesc(eventId);

        if(latestTimeline.isEmpty()){
            RainEventTimeline timeline = new RainEventTimeline();
            timeline.setEventId(eventId);
            timeline.setStartTime(timestamp);
            timeline.setIntensity(newIntensity);
            timelineRepository.save(timeline);
            return;
        }
        RainEventTimeline currentTimeline = latestTimeline.get();

        if(currentTimeline.getIntensity()==newIntensity){
            return;
        }
        currentTimeline.setEndTime(timestamp);
        timelineRepository.save(currentTimeline);
        RainEventTimeline newTimeline = new RainEventTimeline();
        newTimeline.setEventId(eventId);
        newTimeline.setStartTime(timestamp);
        newTimeline.setIntensity(newIntensity);
        timelineRepository.save(newTimeline);
    }

    public void closeCurrentTimeline(Long eventId, LocalDateTime endTime){
        Optional<RainEventTimeline> latestTimeline = timelineRepository.findTopByEventIdOrderByStartTimeDesc(eventId);
        if(latestTimeline.isPresent()){
            RainEventTimeline timeline = latestTimeline.get();
            if(timeline.getEndTime() == null){
                timeline.setEndTime(endTime);
                timelineRepository.save(timeline);
            }
        }
    }
}
