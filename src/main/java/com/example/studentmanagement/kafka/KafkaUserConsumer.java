package com.example.studentmanagement.kafka;

import lombok.val;
import org.springframework.stereotype.Component;
import reactor.kafka.receiver.ReceiverOffset;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class KafkaUserConsumer extends BaseKafkaConsumer<User> {

    private final static List<String> TOPICS = new ArrayList<>(Arrays.asList("users"));

    @PostConstruct
    public void processUsers() {

        val kafkaReceiver = getKafkaReceiver(User.class, TOPICS, "gr2");
        val kafkaFlux = kafkaReceiver.receive();
        kafkaFlux.subscribe(record -> {
            ReceiverOffset offset = record.receiverOffset();
            System.out.println("consuming from kafka, " +
                    "topic-partition " + offset.topicPartition() + ", " +
                    "offset " + offset.offset() + ", " +
                    "value " + record.value()
            );
            offset.acknowledge();
        });
    }

}
