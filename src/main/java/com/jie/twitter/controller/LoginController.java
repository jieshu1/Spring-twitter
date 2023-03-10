package com.jie.twitter.controller;

import com.jie.twitter.entity.User;
import com.jie.twitter.entity.UserSession;
import com.jie.twitter.service.UserService;
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
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/twitter/login", method = RequestMethod.POST)
    public ResponseEntity<Object> logIn(@RequestBody User user){
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad input");
        }

        Map<String, Object> map = new HashMap<String, Object>();
        HttpStatus status;
        String message;
        UserSession userSession;
        if (userService.validateUser(user)){
            status = HttpStatus.CREATED;
            userSession = userService.login(user);
            message = "Successfully logged in";
        }
        else {
            status = HttpStatus.BAD_REQUEST;
            userSession = null;
            message = "Username and password don't match";

        }
        map.put("status", status.value());
        map.put("session", userSession);
        map.put("message", message);
        return new ResponseEntity<Object>(map,status);

    }
}
