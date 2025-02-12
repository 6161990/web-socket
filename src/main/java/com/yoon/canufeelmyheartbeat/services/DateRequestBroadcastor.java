package com.yoon.canufeelmyheartbeat.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.yoon.canufeelmyheartbeat.constant.Topics.DATE_REQUEST_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class DateRequestBroadcastor {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void broadcast(String userId, Map<String, String> payload) {
        try {
            String message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(DATE_REQUEST_TOPIC, userId, message);
            log.info("publishToKafka 'date-request' {}",message);
        } catch (Exception e) {
            throw e;
        }
    }
}
