package com.springboot.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LocationTriggerProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationTriggerProducer.class);
    private KafkaTemplate<String, String> kafkaTemplate;

    public LocationTriggerProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void startFetchingLocation(String id) {
        kafkaTemplate.send("location-start", id);
    }

    public void stopFetchingLocation(String id) {
        kafkaTemplate.send("location-stop", id);
    }
}
