package com.jie.twitter.controller;

import com.jie.twitter.entity.Comment;
import com.jie.twitter.entity.Tweet;
import com.jie.twitter.entity.User;
import com.jie.twitter.entity.UserProfile;
import com.jie.twitter.exception.IdNotFoundException;
import com.jie.twitter.service.UserService;
import com.jie.twitter.utils.EmailFormat;
import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    List<HttpMessageConverter<?>> converters;

    @RequestMapping(value = "twitter/userprofile/", method = RequestMethod.POST)
    public ResponseEntity update(@RequestBody UserProfile userProfile) throws Exception{        EmailFormat.setEmailFormat(userProfile);
        EmailFormat.setEmailFormat(userProfile);
        String email = userProfile.getEmail();
        if (email == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("input data not right ");
        }
        userService.updateUserProfile(userProfile);
        Map<String, Object> map = new HashMap<String, Object>();
        HttpStatus status;
        status = HttpStatus.CREATED;
        map.put("status", status.value());
        map.put("message", "Successfully updated");
        map.put("user profile", userProfile);
        return new ResponseEntity<Object>(map,status);
    }

    @RequestMapping(value = "twitter/userprofile/", method = RequestMethod.GET)
    public ModelAndView getUserProfile(@RequestBody UserProfile userProfile) throws Exception{
        EmailFormat.setEmailFormat(userProfile);
        String email = userProfile.getEmail();
        System.out.println("request received to view user profile for user:" + email);
        userService.validateEmail(email);
        HttpStatus status = HttpStatus.NOT_FOUND;
        HttpHeaders httpHeaders = new HttpHeaders();
        userProfile = userService.getUserProfile(email);
        String path = userProfile.getAvatar();
        System.out.println("Avatar path:" + path);
        ModelAndView result = new ModelAndView("user_profile");
        result.addObject("email", email);
        result.addObject("nickname", userProfile.getNickname());
        result.addObject("url", path);
        System.out.println("Successfully loaded user profile");
        return result;
    }

    /* Problem to directly return image: No converter for [class [B] with preset Content-Type 'image/jpeg'

    @RequestMapping(value = "twitter/userprofile/avatar", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.IMAGE_JPEG_VALUE)
    public HttpEntity<byte[]> getAvatar(@RequestBody UserProfile userProfile) throws Exception {
        EmailFormat.setEmailFormat(userProfile);
        String email = userProfile.getEmail();
        System.out.println("request received to view user profile for user:" + email);
        userService.validateEmail(email);
        HttpStatus status = HttpStatus.NOT_FOUND;
        HttpHeaders httpHeaders = new HttpHeaders();
        userProfile = userService.getUserProfile(userProfile);
        String path = userProfile.getAvatar();
        System.out.println("Avatar path:" + path);
        byte[] img = null;
        UrlValidator validator = new UrlValidator();
        if (validator.isValid(path)){
            URL url = new URL(path);
            InputStream in = url.openStream();
            //InputStream in = getClass().getResourceAsStream(path);
            img = IOUtils.toByteArray(in);
            httpHeaders.setContentType(MediaType.IMAGE_JPEG);
            in.close();
            status = HttpStatus.OK;
            System.out.println("Successfully loaded avatar");
        }
        else {
            System.out.println("Failed loading avatar: url path wrong");
        }
        return new ResponseEntity<>(img, httpHeaders, status);
    }

     */
}
