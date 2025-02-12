package com.yoon.canufeelmyheartbeat;

import com.yoon.canufeelmyheartbeat.dependencies.DateRequestKafkaStreamsService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;


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

    public static void main(String[] args) {
        SpringApplication.run(CanUFeelMyHeartbeatApplication.class, args);
    }

}
