package com.jie.twitter.exception;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler{

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(SQLException.class)
    public String handleSQLException(HttpServletRequest request, Exception ex){
        logger.info("SQLException Occurred:: URL="+request.getRequestURL());
        return "database_error";
    }

    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="IOException occurred")
    @ExceptionHandler(IOException.class)
    public void handleIOException(){
        logger.error("IOException handler executed");
        //returning 404 error code
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(HttpServletRequest request, Exception ex){
        logger.info("User not found Exception Occurred:: URL="+request.getRequestURL());
        return "usernotfound_error";
    }

    @ExceptionHandler(IdNotFoundException.class)
    public String handleIdNotFoundException(HttpServletRequest request, Exception ex){
        logger.info("User not found Exception Occurred:: URL="+request.getRequestURL());
        return "idnotfound_error";
    }


}
