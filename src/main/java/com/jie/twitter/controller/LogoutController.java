package com.jie.twitter.controller;

import com.jie.twitter.entity.User;
import com.jie.twitter.service.UserService;
import com.jie.twitter.entity.UserSession;
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
public class LogoutController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/twitter/logout", method = RequestMethod.POST)
    public ResponseEntity logout(@RequestBody UserSession userSession){
        User user = userService.logout(userSession.getId());
        System.out.println("finish calling userservice to log out for user:" + user.getEmail());
        HttpStatus status;
        String message;
        Map<String, Object> map = new HashMap<String, Object>();
        if (user == null){
            status = HttpStatus.BAD_REQUEST;
            message = "Failed logged out";
        }
        else {
            status = HttpStatus.OK;
            message = "Successfully logged out" + user.getEmail();
        }
        map.put("status",status);
        map.put("message", message);
        return new ResponseEntity<Object>(map, status);
    }
}
