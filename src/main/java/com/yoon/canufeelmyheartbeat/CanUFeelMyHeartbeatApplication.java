package com.yoon.canufeelmyheartbeat;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import static com.yoon.canufeelmyheartbeat.constant.Topics.DATE_REQUEST_TOPIC;

@SpringBootApplication
public class CanUFeelMyHeartbeatApplication {

    @Bean
    public KafkaContainer kafkaContainer(){
        return new KafkaContainer(DockerImageName.parse("apache/kafka-native:3.8.1"));
    }

    @Bean
    public DynamicPropertyRegistrar dynamicPropertyRegistrar(KafkaContainer kafkaContainer) {
        return registry -> {
            registry.add("spring.kafka.consumer.bootstrap-servers", kafkaContainer::getBootstrapServers);
            registry.add("spring.kafka.producer.bootstrap-servers", kafkaContainer::getBootstrapServers);
        };
    }

    @Bean
    public ApplicationRunner eventPublishApplicationRunner(KafkaTemplate<String, Object> kafkaTemplate){
        return args -> kafkaTemplate.send(DATE_REQUEST_TOPIC, "700248805", "{\"message\":\"saaaa\"}");
    }

    public static void main(String[] args) {
        SpringApplication.run(CanUFeelMyHeartbeatApplication.class, args);
    }

}
