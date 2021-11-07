package com.example.studentmanagement.kafka;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

import java.util.HashMap;
import java.util.Map;

@Slf4j

public class BaseKafkaProducer <T> {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private final String topic;
    private final KafkaSender<String, T> sender;

    private final Class<T> tClass;

    public BaseKafkaProducer(Class<T> tClass, String topic) {
        this.tClass = tClass;
        this.topic = topic;

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, Long.MAX_VALUE);
        props.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        if (tClass == String.class) {
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        } else {
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        }
        SenderOptions<String, T> senderOptions = SenderOptions.create(props);
        senderOptions.scheduler(Schedulers.boundedElastic());

        sender = KafkaSender.create(senderOptions);
    }

    public void sendMessages(T message) {
        Mono<SenderRecord<String, T, Object>> senderRecords = Mono.just(message)
                .map(s -> SenderRecord.create(new ProducerRecord<>(topic, s), null));
        sender.send(senderRecords)
                .doOnError(e -> log.error("Send failed ", e))
                .subscribe();
    }



}
