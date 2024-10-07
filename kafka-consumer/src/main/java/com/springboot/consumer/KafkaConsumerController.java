package com.springboot.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
public class KafkaConsumerController {

    @Autowired
    private KafkaLocationConsumer kafkaLocationConsumer;

    @Autowired
    private LocationTriggerProducer locationTriggerProducer;

    @GetMapping(value = "/location/stream/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ObjectCoordinates> getLocationStream(@PathVariable String id) {
        kafkaLocationConsumer.registerSink(id);
        locationTriggerProducer.startFetchingLocation(id);
        return kafkaLocationConsumer.getEventStream(id).doFinally(signal -> {
            // Remove the sink when the client disconnects and stop API calls if no more clients exist
            kafkaLocationConsumer.removeSink(id);
            if (!kafkaLocationConsumer.containsSink(id)) {
                locationTriggerProducer.stopFetchingLocation(id);
            }
        });
    }
}
