package com.jie.twitter.controller;

import com.jie.twitter.entity.Comment;
import com.jie.twitter.entity.Tweet;
import com.jie.twitter.entity.User;
import com.jie.twitter.exception.IdNotFoundException;
import com.jie.twitter.exception.UserNotFoundException;
import com.jie.twitter.service.CommentService;
import com.jie.twitter.service.TweetService;
import com.jie.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private TweetService tweetService;

    @RequestMapping(value = "twitter/comment", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Map<String, String> params) throws Exception{
        String email = params.get("email");
        String content = params.get("content");
        Integer tweet_id = Integer.valueOf(params.get("tweet_id"));
        if (email == null || content == null || tweet_id == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("input data not right: " +
                    params
            );
        }
        Comment comment = new Comment();
        User user = new User();
        user.setEmail(email);
        if (userService.validateUsername(user) == false){
            throw new UserNotFoundException(user.getEmail());
        }
        Tweet tweet = new Tweet();
        tweet.setId(tweet_id);
        tweetService.validateTweetId(tweet);
        comment.setContent(content);
        comment.setUser(user);
        comment.setTweet(tweet);
        Map<String, Object> map = new HashMap<String, Object>();
        HttpStatus status;
        commentService.createOrUpdate(comment);
        status = HttpStatus.CREATED;
        map.put("status", status.value());
        map.put("message", "Successfully created comments");
        map.put("comment", comment);
        return new ResponseEntity<Object>(map,status);

    }

    @RequestMapping(value = "twitter/comment/{id}/delete", method = RequestMethod.POST)
    public ResponseEntity delete(@PathVariable("id") Integer id) throws Exception{
        if (id == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("input data not right ");
        }
        Comment comment = new Comment();
        comment.setId(id);
        commentService.validateCommentId(comment);
        Map<String, Object> map = new HashMap<String, Object>();
        HttpStatus status;
        commentService.destroy(comment);
        status = HttpStatus.CREATED;
        map.put("status", status.value());
        map.put("message", "Successfully deleted");
        map.put("comment", comment);
        return new ResponseEntity<Object>(map,status);
    }

    @RequestMapping(value = "twitter/comment/update", method = RequestMethod.POST)
    public ResponseEntity update(@RequestBody Comment updatedComment) throws Exception{
        Integer id = updatedComment.getId();
        if (id == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("input data not right ");
        }
        Comment comment = commentService.getById(id);
        if (comment == null) {
            throw new IdNotFoundException(id);
        }
        comment.setContent(updatedComment.getContent());
        commentService.createOrUpdate(comment);
        Map<String, Object> map = new HashMap<String, Object>();
        HttpStatus status;
        status = HttpStatus.CREATED;
        map.put("status", status.value());
        map.put("message", "Successfully updated");
        map.put("comment", comment);
        return new ResponseEntity<Object>(map,status);
    }


    @RequestMapping(value = "twitter/tweet/{id}/comments", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getComments(@PathVariable("id") Integer id) throws Exception{
        Tweet tweet = new Tweet();
        tweet.setId(id);
        System.out.println("request received to view comments for tweet:" + id);
        tweetService.validateTweetId(tweet);
        Map<String, Object> map = new HashMap<String, Object>();
        HttpStatus status;
        String message;
        commentService.getComments(tweet);
        status = HttpStatus.OK;
        message = "Successfully loaded comments list";
        map.put("status", status.value());
        map.put("message", message);
        map.put("commentsList", tweet.getCommentsList());
        return new ResponseEntity<Object>(map,status);
    }
}
