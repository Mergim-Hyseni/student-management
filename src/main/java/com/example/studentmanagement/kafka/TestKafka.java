package com.example.studentmanagement.kafka;

import com.example.studentmanagement.kafka.BaseKafkaProducer;
import com.example.studentmanagement.kafka.User;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestKafka {

    private final BaseKafkaProducer kafkaStringProducer = new BaseKafkaProducer(String.class, "message");
    private final BaseKafkaProducer kafkaObjectProducer = new BaseKafkaProducer(User.class, "users");

    @GetMapping("/{message}")
    public void publishMessageToKafka(@PathVariable String message) {
        kafkaStringProducer.sendMessages(message);
    }

    @PostMapping("user")
    public void publishUserToKafka(@RequestBody User user){
        System.out.println("sending user to kafka " + user.toString());
        kafkaObjectProducer.sendMessages(user);
    }
}
