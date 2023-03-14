package com.jie.twitter.utils;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class KafkaClientStringProducer {

    @Autowired
    private Properties kafkaStringProperties;
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);


    public void sendMessage(String topic, String message) {

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(kafkaStringProperties);

        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, message);
        producer.send(record);
        LOGGER.info(String.format("message received -> %s", message));
        producer.close();
    }

}
