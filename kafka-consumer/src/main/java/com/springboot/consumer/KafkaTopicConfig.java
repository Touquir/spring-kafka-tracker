package com.springboot.consumer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic locationStart() {
        return TopicBuilder.name("location_start").build();
    }

    @Bean
    public NewTopic locationStop() {
        return TopicBuilder.name("location_stop").build();
    }
}
