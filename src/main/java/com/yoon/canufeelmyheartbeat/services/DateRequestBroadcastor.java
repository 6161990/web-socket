package com.yoon.canufeelmyheartbeat.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.yoon.canufeelmyheartbeat.constant.Topics.DATE_REQUEST_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class DateRequestBroadcastor {

    private final KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void broadcast(String userId, Map<String, String> payload) {
        try {
            String message = objectMapper.writeValueAsString(payload);
            producer.send(new ProducerRecord<>(DATE_REQUEST_TOPIC, userId, message));
            log.info("publishToKafka 'date-request' {}",message);
        } catch (Exception e) {
            throw e;
        }
    }
}
