package com.yoon.canufeelmyheartbeat.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.api.ContextualProcessor;
import org.apache.kafka.streams.processor.api.Record;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static com.yoon.canufeelmyheartbeat.constant.Topics.WS_SUB_DATE_REQUEST_ALARM_TOPIC;

/**
KIn: 입력 메시지의 키 타입
VIn: 입력 메시지의 값 타입
KOut: 출력 메시지의 키 타입
VOut: 출력 메시지의 값 타입
* */

@Slf4j
@Component
@RequiredArgsConstructor
public class DateRequestAlarmProcessor extends ContextualProcessor<String, String, String, String> {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void process(Record<String, String> record) {
        log.info("MyCustomProcessor {}", record);

        // 메시지를 대문자로 변환
        String modifiedValue = record.value().toUpperCase().concat("\n 데이트 신청 알람이 도착했어요!");

        // 수정된 메시지를 다음 단계로 전달하는 부분. sink 단계가 불필요하므로 생략
//        context().forward(new Record<>(record.key(), modifiedValue, record.timestamp()));

        messagingTemplate.convertAndSend(WS_SUB_DATE_REQUEST_ALARM_TOPIC, modifiedValue);

        // 메시지 처리 완료
        context().commit();
    }

}

