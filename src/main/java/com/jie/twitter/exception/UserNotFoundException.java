package com.jie.twitter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="User Not Found")
public class UserNotFoundException extends Exception {

    private static final long serialVersionUID = -3332292346834265371L;

    public UserNotFoundException(String email){
        super("UserNotFoundException with email="+email);
    }
}
