package com.example.studentmanagement.kafka;


import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BaseKafkaConsumer <T> {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    private List<String> topics;
    private String group;

    private ReceiverOptions<String, T> receiverOptions;


   public KafkaReceiver<String, T> getKafkaReceiver(Class<T> tClass, List<String> topics, String group) {
       this.topics = topics;
       this.group = group;
       Map<String, Object> props = new HashMap<>();
       props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
       props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
       props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
       if (tClass == String.class) {
           props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
       } else {
           props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
       }
       props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
       props.put(JsonDeserializer.TRUSTED_PACKAGES,"*");

       receiverOptions = ReceiverOptions.create(props);
       ReceiverOptions<String, T> options = receiverOptions.subscription(topics);
       val kafkaReceiver = KafkaReceiver.create(options);

       return kafkaReceiver;
   }




}
