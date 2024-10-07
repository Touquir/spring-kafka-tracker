package com.springboot.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class LocationTriggerConsumer {
    @Autowired
    private KafkaLocationProducer kafkaLocationProducer;
    @Autowired
    private MapLocationService mapLocationService;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Map<String, ScheduledFuture<?>> locationTasks = new HashMap<>();

    @KafkaListener(
            topics = "location_start",
            groupId = "myGroup"
    )
    public void startFetchingLocation(String id) {
        if(!locationTasks.containsKey(id)) {
            ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(() -> {
                ObjectCoordinates location = mapLocationService.getCurrentLocation(id);
                kafkaLocationProducer.sendCoordinates(location);
            },0,5, TimeUnit.SECONDS);
            locationTasks.put(id, task);
        }
    }

    @KafkaListener(
            topics = "location_stop",
            groupId = "myGroup"
    )
    public void stopFetchingLocation(String id) {
        locationTasks.get(id).cancel(true);
        locationTasks.remove(id);
    }
}
