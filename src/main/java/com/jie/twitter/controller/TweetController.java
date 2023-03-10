package com.jie.twitter.controller;

import com.jie.twitter.entity.User;
import com.jie.twitter.service.TweetService;
import com.jie.twitter.service.UserService;
import com.jie.twitter.entity.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TweetController {

    @Autowired
    private UserService userService;

    @Autowired
    private TweetService tweetService;

    @RequestMapping(value = "twitter/tweet", method = RequestMethod.POST)
    public ResponseEntity createTweet(@RequestBody Map<String, String> params){
        String email = params.get("email");
        String content = params.get("content");
        if (email == null || content == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("input data not right: " +
                    params
                    );
        }
        Tweet tweet = new Tweet();
        User user = new User();
        user.setEmail(email);
        tweet.setContent(content);
        tweet.setUser(user);
        Map<String, Object> map = new HashMap<String, Object>();
        HttpStatus status;
        if (tweetService.createTweet(tweet)){
            status = HttpStatus.CREATED;
            map.put("status", status.value());
            map.put("message", "Successfully created tweets");
            map.put("tweet", tweet);


        }
        else {
            status = HttpStatus.BAD_REQUEST;
            map.put("status", status.value());
            map.put("message", "Failed");

        }
        return new ResponseEntity<Object>(map,status);

    }

    @RequestMapping(value = "twitter/tweet", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getTweet(@RequestParam("email") String email){
        User user = new User();
        user.setEmail(email);
        System.out.println("request received to view tweets for user:" + email);
        Map<String, Object> map = new HashMap<String, Object>();
        HttpStatus status;
        String message;
        if (tweetService.getTweets(user)){
            status = HttpStatus.OK;
            message = "Successfully loaded tweets list";
        }
        else {
            status = HttpStatus.BAD_REQUEST;
            message = "Failed";
        }
        map.put("status", status.value());
        map.put("message", message);
        map.put("tweetsList", user.getTweetsList());
        return new ResponseEntity<Object>(map,status);
    }
}
