package com.yoon.canufeelmyheartbeat.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorSupplier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaStreamsProcessorSupplier implements ProcessorSupplier<Object, Object, Object, Object> {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Processor<Object, Object, Object, Object> get() {
        return new DateRequestAlarmProcessor(simpMessagingTemplate, objectMapper);
    }

}
