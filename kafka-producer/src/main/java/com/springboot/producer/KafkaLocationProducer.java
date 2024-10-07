package com.springboot.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaLocationProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaLocationProducer.class);
    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaLocationProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCoordinates(ObjectCoordinates objectCoordinates) {
        String topic = "location_tracker";
        LOGGER.info(String.format("sent coordinates for: %s %d %d",objectCoordinates.getId(), objectCoordinates.getLat(),objectCoordinates.getLng()));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writeValueAsString(objectCoordinates);
            kafkaTemplate.send(topic, jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
