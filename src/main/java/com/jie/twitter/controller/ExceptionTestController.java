package com.jie.twitter.controller;

import com.jie.twitter.exception.GlobalExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@Controller
public class ExceptionTestController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @RequestMapping(value = "/exceptions/{id}", method = RequestMethod.GET)
    public String getUser(@PathVariable("id") int id) throws Exception {
        if (id == 1){
            throw new SQLException("SQLException, id=" + id);
        }else if(id == 2){
            throw new IOException("IOException, id="+id);
        }else if(id == 3){
            return "home";
        }else {
            throw new Exception("Generic Exception, id="+id);
        }
    }

}
