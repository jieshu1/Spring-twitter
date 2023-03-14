package com.jie.twitter.controller;

import com.jie.twitter.utils.KafkaClientStringProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitter/kafka")
public class MessageController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final static String topic = "kafka_test";

    public void sendMessage(String message) {
        kafkaTemplate.send(topic, message);
    }

    @KafkaListener(topics = topic, groupId = "myGroup")
    public void consume(String message){
        System.out.println(String.format("Topic %s, Message received: %s", topic,message));
    }
    // http:localhost:8081/twitter/kafka/publish?message=hello world
    @GetMapping("/publish")
    public ResponseEntity<String> publish(@RequestParam("message") String message){
        sendMessage(message);
        return ResponseEntity.ok(String.format("Topic %s, Message sent: %s", topic, message));

    }
}
