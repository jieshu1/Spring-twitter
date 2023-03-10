package com.jie.twitter.service;

import com.jie.twitter.entity.User;
import com.jie.twitter.dao.TweetDao;
import com.jie.twitter.entity.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TweetService {
    @Autowired
    private TweetDao tweetDao;

    @Autowired
    private UserService userService;

    @Autowired
    private NewsfeedService newsfeedService;

    public Boolean createTweet(Tweet tweet){
        Boolean result = false;
        User user = tweet.getUser();
        user.setEmail(user.getEmail().toLowerCase());
        if (userService.validateUsername(user) && tweetDao.createTweet(tweet)){
            result = true;
            newsfeedService.fanoutToFollowers(tweet);
        }
        return result;
    }

    public Boolean getTweets(User user){
        user.setEmail(user.getEmail().toLowerCase());
        Boolean result = false;
        if (userService.validateUsername(user)){
            System.out.println("userid validated for user:" + user.getEmail());
            result = true;
            tweetDao.getTweets(user);
        }
        return result;
    }
}
