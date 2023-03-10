package com.jie.twitter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloController {

    @RequestMapping(value = "/twitter", method = RequestMethod.GET)
    public ResponseEntity<String> helloWorld() {
        System.out.println("visited home page");

        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }
}
