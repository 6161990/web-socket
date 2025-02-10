package com.yoon.canufeelmyheartbeat.dependencies;

import com.yoon.canufeelmyheartbeat.services.DateRequestAlarmProcessor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.stereotype.Service;

import static com.yoon.canufeelmyheartbeat.constant.Topics.DATE_REQUEST_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class DateRequestKafkaStreamsService {

    private final KafkaStreamsConfiguration kafkaStreamsConfiguration;
    private final DateRequestAlarmProcessor dateRequestAlarmProcessor;

    /** Kafka Streams의 Processor API를 사용해 Kafka 메시지를 수집, 가공, 발행
    * Topology? Kafka Streams 애플리케이션의 **데이터 처리 흐름(파이프라인)**을 정의하는 객체
     * */
    @PostConstruct
    public void convertAndSend() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        streamsBuilder.stream(DATE_REQUEST_TOPIC)
                .process((ProcessorSupplier<? super Object, ? super Object>) dateRequestAlarmProcessor);

        Topology topology = streamsBuilder.build();

        /*
        Topology topology = new Topology();

        topology.addSource("date request", "date-request") // "Source"는 이 단계의 이름이고, **input-topic**은 메시지를 가져올 Kafka
                .addProcessor("date request processor", DateAlarmRequestProcessor::new, "date request") // 메시지를 가공하거나 비즈니스 로직을 처리

                 WebSocket 메시지 전송이 최종 출력 역할을 대신하기 때문에 얘가 필요없어.
                sink는 Kafka 토픽에 메시지를 다시 저장할 때 사용하니까!
                .addSink("date request sink", "date-request-alarm", "date request processor");*/

        KafkaStreams streams = new KafkaStreams(topology, kafkaStreamsConfiguration.asProperties());
        streams.start();
    }


}

