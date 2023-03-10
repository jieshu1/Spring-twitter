package com.jie.twitter.service;

import com.jie.twitter.dao.NewsfeedDao;
import com.jie.twitter.entity.Newsfeed;
import com.jie.twitter.entity.Tweet;
import com.jie.twitter.entity.User;
import com.jie.twitter.utils.EmailFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsfeedService {
    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private NewsfeedDao newsfeedDao;

    private int batchSize = 5;

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
        newsfeedDao.bulkCreateNewsfeeds(newsfeedsList, batchSize);

    }

    public Boolean getNewsfeeds(User user){
        EmailFormat.setEmailFormat(user);
        return newsfeedDao.getNewsfeeds(user);
    }
}
