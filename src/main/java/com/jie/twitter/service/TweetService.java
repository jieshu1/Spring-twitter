package com.jie.twitter.service;

import com.jie.twitter.entity.User;
import com.jie.twitter.dao.TweetDao;
import com.jie.twitter.entity.Tweet;
import com.jie.twitter.exception.IdNotFoundException;
import com.jie.twitter.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TweetService {
    @Autowired
    private TweetDao tweetDao;

    @Autowired
    private UserService userService;

    @Autowired
    private NewsfeedService newsfeedService;

    public void createTweet(Tweet tweet) throws Exception{
        User user = tweet.getUser();
        user.setEmail(user.getEmail().toLowerCase());
        if (userService.validateUsername(user) == false){
            throw new UserNotFoundException(user.getEmail());
        }
        tweetDao.createTweet(tweet);
        newsfeedService.fanoutToFollowers(tweet);
    }

    public List<Tweet> getTweets(User user) throws Exception{
        if (userService.validateUsername(user)){
            System.out.println("userid validated for user:" + user.getEmail());
            tweetDao.getTweets(user);
        }
        else {
            throw new UserNotFoundException(user.getEmail());
        }
        return user.getTweetsList();
    }
    public Tweet getbyId(Integer id)
    {
        System.out.println("get tweet by id from database");
        return tweetDao.getById(id);
    }

    public void validateTweetId(Tweet tweet) throws Exception{
        if (tweetDao.getById(tweet.getId()) == null){
            throw new IdNotFoundException(tweet.getId());
        };
    }
}
