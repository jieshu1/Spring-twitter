package com.jie.twitter.service;

import com.jie.twitter.dao.CommentDao;
import com.jie.twitter.entity.Comment;
import com.jie.twitter.entity.Tweet;
import com.jie.twitter.exception.IdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;

@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;

    public void getComments(Tweet tweet) throws Exception{

        commentDao.getComments(tweet);
    }

    public void createOrUpdate(Comment comment) throws Exception{
        commentDao.createOrUpdate(comment);
    }

    public void destroy(Comment comment) throws Exception{
        commentDao.destroy(comment);
    }

    public Comment getById(Integer id){
        return commentDao.getById(id);
    }

    public void validateCommentId(Comment comment) throws Exception{
        if (commentDao.getById(comment.getId()) == null){
            throw new IdNotFoundException(comment.getId());
        };
    }
}
