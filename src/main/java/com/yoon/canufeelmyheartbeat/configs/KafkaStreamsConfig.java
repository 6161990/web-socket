package com.yoon.canufeelmyheartbeat.configs;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.Collections;
import java.util.Properties;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaStreamsConfig {

    private final KafkaProperties kafkaProperties;

    @Bean(name = "kafkaStreamsConfiguration")
    public KafkaStreamsConfiguration kafkaStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put("application.id", "can-u-feel-my-heartbeat");
        props.put("bootstrap.servers", kafkaProperties.getBootstrapServers());
        props.put("default.key.serde", "org.apache.kafka.common.serialization.Serdes$StringSerde");
        props.put("default.value.serde", "org.apache.kafka.common.serialization.Serdes$StringSerde");
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public AdminClient kafkaAdminClient() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        AdminClient adminClient = AdminClient.create(props);
        createTopicIfNotExists(adminClient);
        return adminClient;
    }

    public void createTopicIfNotExists(AdminClient client) {
        try (AdminClient adminClient = client) {
            String topicName = "date-request";
            if (!topicExists(adminClient, topicName)) {
                createTopic(adminClient, topicName, 1, (short) 1);
                log.info("Topic '{}' created successfully.", topicName);
            } else {
                log.info("Topic '{}' already exists.", topicName);
            }
        }
    }

    private boolean topicExists(AdminClient adminClient, String topicName) {
        try {
            return adminClient.listTopics().names().get().contains(topicName);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error checking if topic exists: {}", topicName, e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private void createTopic(AdminClient adminClient, String topicName, int partitions, short replicationFactor) {
        NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
        try {
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error creating topic: {}", topicName, e);
            Thread.currentThread().interrupt();
        }
    }

    @Bean
    public StreamsBuilderFactoryBean streamsBuilderFactoryBean(KafkaStreamsConfiguration kafkaStreamsConfiguration) {
        return new StreamsBuilderFactoryBean(kafkaStreamsConfiguration);
    }
}
