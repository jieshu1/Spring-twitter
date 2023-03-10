package com.jie.twitter.controller;

import com.jie.twitter.entity.User;
import com.jie.twitter.service.NewsfeedService;
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
public class NewsfeedController {
    @Autowired
    private NewsfeedService newsfeedService;

    @RequestMapping(value = "/twitter/newsfeeds/",method = RequestMethod.GET)
    public ResponseEntity getNewsfeeds(@RequestBody User user){
        if (user == null || user.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad input");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        HttpStatus status;
        String message;
        if (newsfeedService.getNewsfeeds(user)){
            status = HttpStatus.OK;
            message = "successful";
        }
        else {
            status = HttpStatus.BAD_REQUEST;
            message = "failed";
        }
        map.put("status", status.value());
        map.put("message", message);
        map.put("newsfeeds", user.getNewsfeedsList());


        return new ResponseEntity<Object>(map,status);
    }
}
