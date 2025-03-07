package com.yoon.canufeelmyheartbeat.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoon.canufeelmyheartbeat.dtos.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.Record;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static com.yoon.canufeelmyheartbeat.constant.Topics.WS_SUB_DATE_REQUEST_ALARM_TOPIC;

@Slf4j
@RequiredArgsConstructor
public class DateRequestAlarmProcessor implements Processor<Object, Object, Object, Object> {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    private ProcessorContext<Object, Object> context;

    @Override
    public void init(ProcessorContext<Object, Object> context) {
        this.context = context;
    }

    @Override
    public void process(Record<Object, Object> record) {
        log.info("Received record: {}", record);

        // 기존 record.value()에 sender 추가
        ChatMessage updatedValue = convertRecord(record.key().toString(), record.value());

        // 수정된 메시지를 다음 단계로 전달하는 부분. sink 단계가 불필요하므로 생략
//            context().forward(new Record<>(record.key(), modifiedValue, record.timestamp()));

        // WebSocket 메시지 전송
        messagingTemplate.convertAndSend(WS_SUB_DATE_REQUEST_ALARM_TOPIC, updatedValue);
    }

    @Override
    public void close() {
        // 리소스 해제 작업 (필요할 경우)
    }

    private ChatMessage convertRecord(String key, Object value) {
        try {
            String jsonString = value.toString().replace("\\\"", "\"").replaceFirst("^\"", "").replaceFirst("\"$", "");

            JsonNode jsonNode = objectMapper.readTree(jsonString);
            String message = jsonNode.has("message") ? jsonNode.get("message").asText() : "No message";
            return new ChatMessage(key, message);

        } catch (Exception e) {
            log.error("Failed to parse or update JSON value: {}", value, e);

            ChatMessage emptyMessage = new ChatMessage(key, "Invalid message");
            return emptyMessage;
        }
    }

}

