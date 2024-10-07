package com.springboot.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaLocationConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaLocationConsumer.class);
    private final Map<String, Sinks.Many<ObjectCoordinates>> sinks = new HashMap<>();

    @KafkaListener(
            topics = "location_tracker",
            groupId = "myGroup"
    )
    public void consumeCoordinates(String coordinatesString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectCoordinates objectCoordinates = objectMapper.readValue(coordinatesString, ObjectCoordinates.class);
            String locationId = objectCoordinates.getId();
            this.registerSink(locationId);
            sinks.get(locationId).tryEmitNext(objectCoordinates);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void registerSink(String id) {
        if (!sinks.containsKey(id)) {
            sinks.put(id, Sinks.many().multicast().onBackpressureBuffer());
        }
    }

    public void removeSink(String id) {
        sinks.remove(id);
    }

    public boolean containsSink(String id) {
        return sinks.containsKey(id);
    }

    public Flux<ObjectCoordinates> getEventStream(String id) {
        return sinks.get(id).asFlux();
    }
}
