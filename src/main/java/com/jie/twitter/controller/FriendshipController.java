package com.jie.twitter.controller;

import com.jie.twitter.entity.Friendship;
import com.jie.twitter.entity.User;
import com.jie.twitter.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@Controller
public class FriendshipController {
    @Autowired
    private FriendshipService friendshipService;

    @RequestMapping(value = "/twitter/follow", method = RequestMethod.POST)
    public ResponseEntity<Object> follow(@RequestBody Friendship friendship){
        if (friendship == null || friendship.getFromUser() == null || friendship.getToUser() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad input");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        HttpStatus status;
        String message;
        if (friendshipService.follow(friendship)){
            status = HttpStatus.CREATED;
            message = "successfully followed";
        }
        else {
            status = HttpStatus.BAD_REQUEST;
            message = "failed";
        }
        map.put("status", status.value());
        map.put("message", message);
        map.put("friendship", friendship);

        return new ResponseEntity<Object>(map,status);

    }

    @RequestMapping(value = "/twitter/unfollow", method = RequestMethod.POST)
    public ResponseEntity<Object> unfollow(@RequestBody Friendship friendship){
        if (friendship == null || friendship.getFromUser() == null || friendship.getToUser() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad input");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        HttpStatus status;
        String message;
        if (friendshipService.unfollow(friendship)){
            status = HttpStatus.CREATED;
            message = "successfully unfollowed";
        }
        else {
            status = HttpStatus.BAD_REQUEST;
            message = "failed";
        }
        map.put("status", status.value());
        map.put("message", message);
        map.put("friendship", friendship);


        return new ResponseEntity<Object>(map,status);

    }

    @RequestMapping(value = "/twitter/followers", method = RequestMethod.GET)
    public ResponseEntity<Object> getFollowers(@RequestBody User user){
        if (user == null || user.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad input");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        HttpStatus status;
        String message;
        if (friendshipService.getFollowers(user)){
            status = HttpStatus.OK;
            message = "successfully followed";
        }
        else {
            status = HttpStatus.BAD_REQUEST;
            message = "failed";
        }
        map.put("status", status.value());
        map.put("message", message);
        map.put("followers", user.getFollowersList());


        return new ResponseEntity<Object>(map,status);

    }

    @RequestMapping(value = "/twitter/followings", method = RequestMethod.GET)
    public ResponseEntity<Object> getFollowings(@RequestBody User user){
        if (user == null || user.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad input");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        HttpStatus status;
        String message;
        if (friendshipService.getFollowings(user)){
            status = HttpStatus.OK;
            message = "successfully followed";
        }
        else {
            status = HttpStatus.BAD_REQUEST;
            message = "failed";
        }
        map.put("status", status.value());
        map.put("message", message);
        map.put("followers", user.getFollowingsList());


        return new ResponseEntity<Object>(map,status);

    }
}
