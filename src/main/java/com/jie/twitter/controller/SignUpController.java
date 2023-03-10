package com.jie.twitter.controller;

import com.jie.twitter.service.UserService;
import com.jie.twitter.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SignUpController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/twitter/signup", method = RequestMethod.POST)
    // @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<String> signUp(@RequestBody User user) {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad input");
        }
        System.out.println("signup request received:" + user.getEmail());
        if (userService.getUser(user.getEmail()) != null) {
                System.out.println(("email already registered"));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already registered");
            }
        userService.signUp(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully signed up");
    }
}
