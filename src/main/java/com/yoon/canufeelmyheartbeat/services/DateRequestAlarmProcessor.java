package com.yoon.canufeelmyheartbeat.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.ProcessorSupplier;
import org.apache.kafka.streams.processor.api.Record;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static com.yoon.canufeelmyheartbeat.constant.Topics.WS_SUB_DATE_REQUEST_ALARM_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
public class DateRequestAlarmProcessor implements ProcessorSupplier<String, String, String, String> {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Processor<String, String, String, String> get() {
        return new Processor<>() {
            @Override
            public void init(ProcessorContext<String, String> context) {
                Processor.super.init(context);
            }

            @Override
            public void process(Record<String, String> record) {
                log.info("Received record: {}", record);

                // 기존 record.value()에 sender 추가
                String updatedValue = addSenderToValue(record.key(), record.value());

                // 수정된 메시지를 다음 단계로 전달하는 부분. sink 단계가 불필요하므로 생략
//            context().forward(new Record<>(record.key(), modifiedValue, record.timestamp()));

                // WebSocket 메시지 전송
                messagingTemplate.convertAndSend(WS_SUB_DATE_REQUEST_ALARM_TOPIC, updatedValue);
            }

            @Override
            public void close() {
                Processor.super.close();
            }
        };
    }

    private String addSenderToValue(String key, String value) {
        try {
            JsonNode jsonNode = objectMapper.readTree(value);

            // JSON이 Object 형태가 아니라면 무시
            if (!jsonNode.isObject()) {
                log.warn("Record value is not a JSON object: {}", value);
                return value;
            }

            // 기존 JSON에 sender 필드 추가
            ObjectNode updatedJson = (ObjectNode) jsonNode;
            updatedJson.put("sender", key);

            return objectMapper.writeValueAsString(updatedJson);
        } catch (Exception e) {
            log.error("Failed to parse or update JSON value: {}", value, e);
            return value;
        }
    }
}
