package com.jie.twitter.kafka;

import com.alibaba.fastjson2.JSON;
import com.jie.twitter.dao.NewsfeedDao;
import com.jie.twitter.entity.Newsfeed;
import com.jie.twitter.entity.Tweet;
import com.jie.twitter.entity.User;
import com.jie.twitter.utils.FanoutMessage;
import com.jie.twitter.utils.KafkaClientStringProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

@Service
@EnableKafka
public class NewsfeedConsumer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private NewsfeedDao newsfeedDao;
    private static final String TOPIC_TASK = "newsfeeds_fanout";
    private static final String TOPIC_BATCH = "newsfeeds_fanout_batch";
    private static final Logger LOGGER = LoggerFactory.getLogger(org.apache.kafka.clients.consumer.KafkaConsumer.class);

    @KafkaListener(topics = TOPIC_TASK, groupId = "myGroup")
    public void consumeTask(String message) {
        LOGGER.info(String.format("listener topic: $s, message received -> %s", TOPIC_TASK, message));
        FanoutMessage fanoutMessage = JSON.parseObject(message, FanoutMessage.class);
        System.out.println(String.format("received message on topic: %s, string to object %s",
                TOPIC_TASK, fanoutMessage.toString()));
        int i = 0;
        List<Newsfeed> newsfeedsList = new LinkedList<>();
        FanoutMessage taskMessage = new FanoutMessage(newsfeedsList, fanoutMessage.getBatchSize(), fanoutMessage.getTaskSize());
        if (fanoutMessage.getTaskSize() == 0 || fanoutMessage.getBatchSize() == 0 || fanoutMessage.getNewsfeedsList() == null) {
            return;
        }
        int taskno = 0;
        while (i < fanoutMessage.getNewsfeedsList().size()) {
            newsfeedsList.add(fanoutMessage.getNewsfeedsList().get(i));
            i++;
            if (i % fanoutMessage.getTaskSize() == 0 || i == fanoutMessage.getNewsfeedsList().size()) {
                taskno += 1;
                kafkaTemplate.send(TOPIC_BATCH, JSON.toJSONString(taskMessage));
                System.out.println(String.format("sent out topic %s: task: %s", TOPIC_BATCH, taskno));
                newsfeedsList = new LinkedList<>();
                taskMessage = new FanoutMessage(newsfeedsList, fanoutMessage.getBatchSize(), fanoutMessage.getTaskSize());
            }
        }
    }

    @KafkaListener(topics = TOPIC_BATCH, groupId = "myGroup")
    public void consumeBatch(String message) {
        LOGGER.info(String.format("listener topic: $s, message received -> %s", TOPIC_BATCH, message));
        FanoutMessage fanoutMessage = JSON.parseObject(message, FanoutMessage.class);
        System.out.println(String.format("newsfeeds_fanout batch message received: %s", fanoutMessage.toString()));
        List<Newsfeed> newsfeedsList = fanoutMessage.getNewsfeedsList();
        newsfeedDao.bulkCreateNewsfeeds(newsfeedsList, fanoutMessage.getBatchSize());
    }
}
