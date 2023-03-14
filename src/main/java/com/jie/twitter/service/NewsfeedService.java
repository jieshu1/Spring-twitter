package com.jie.twitter.service;

import com.alibaba.fastjson2.JSON;
import com.jie.twitter.dao.NewsfeedDao;
import com.jie.twitter.entity.Newsfeed;
import com.jie.twitter.entity.Tweet;
import com.jie.twitter.entity.User;
import com.jie.twitter.utils.KafkaClientStringProducer;
import com.jie.twitter.utils.EmailFormat;
import com.jie.twitter.utils.FanoutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class NewsfeedService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private NewsfeedDao newsfeedDao;

    private static final String TOPIC_TASK = "newsfeeds_fanout";
    private int batchSize = 2;
    private int taskSize = 4;

    public void fanoutToFollowers(Tweet tweet){
        List<Newsfeed> newsfeedsList = new ArrayList<>();
        Newsfeed newsfeed = new Newsfeed();
        User user = tweet.getUser();
        newsfeed.setUser(user);
        newsfeed.setTweet(tweet);
        newsfeedsList.add(newsfeed);
        friendshipService.getFollowers(user);
        for (int i = 0; i < user.getFollowersList().size(); i++){
            newsfeed = new Newsfeed();
            newsfeed.setUser(user.getFollowersList().get(i));
            newsfeed.setTweet(tweet);
            newsfeedsList.add(newsfeed);
        }
        //newsfeedDao.bulkCreateNewsfeeds(newsfeedsList, batchSize);
        FanoutMessage fanoutMessage = new FanoutMessage(newsfeedsList, batchSize, taskSize);
        System.out.println(String.format("Send message to topic: %s, object to string %s",
                TOPIC_TASK, JSON.toJSONString(fanoutMessage)));
        kafkaTemplate.send(TOPIC_TASK, JSON.toJSONString(fanoutMessage));
        for (int i = 0; i < fanoutMessage.getNewsfeedsList().size(); i++){
            newsfeed = newsfeedsList.get(i);
            tweet = newsfeed.getTweet();
            user = newsfeed.getUser();
            System.out.println(String.format("send topic %s: fanout newsfeed: user %s, " +
                    "tweet from user %s, content %s",  TOPIC_TASK, user.getEmail(), tweet.getUser().getEmail(), tweet.getContent()));
        }

    }

    public Boolean getNewsfeeds(User user){
        EmailFormat.setEmailFormat(user);
        return newsfeedDao.getNewsfeeds(user);
    }
}
