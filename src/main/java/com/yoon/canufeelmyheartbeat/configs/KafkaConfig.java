package com.yoon.canufeelmyheartbeat.configs;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaProducer<String, String> kafkaProducer(){
        Map<String, Object> props = new HashMap<>();
        return new KafkaProducer<>(props);
    }
}
