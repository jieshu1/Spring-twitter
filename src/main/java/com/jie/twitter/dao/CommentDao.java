package com.jie.twitter.dao;

import com.jie.twitter.entity.Comment;
import com.jie.twitter.entity.Tweet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommentDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void createOrUpdate(Comment comment) throws Exception{
        Session session = null;
        try{
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(comment);
            session.getTransaction().commit();
            System.out.println(String.format("comment created/updated: %s", comment));

        } catch (Exception ex){
            ex.printStackTrace();
            session.getTransaction().rollback();
            throw new SQLException("SQLException for comment id=" + comment.getId());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void destroy(Comment comment) throws Exception{
        Session session = null;
        try{
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.delete(comment);
            session.getTransaction().commit();
            System.out.println(String.format("comment deleted"));

        } catch (Exception ex){
            ex.printStackTrace();
            session.getTransaction().rollback();
            throw new SQLException("SQLException for comment id=" + comment.getId());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void getComments(Tweet tweet) throws Exception{
        List<Comment> commentsList = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            String hql = "from Comment comment where comment.tweet=:tweet";
            Query theQuery = session.createQuery(hql);
            theQuery.setParameter("tweet", tweet);
            commentsList = theQuery.getResultList();
            if (commentsList.size() > 0){
                tweet.setCommentsList(commentsList);
                System.out.println("tweetID:" + tweet.getId() +
                        ", has comments:" + tweet.getCommentsList());
            }
            else {
                System.out.println("tweetID:" + tweet.getId() +
                        "has no comments");
            }
        } catch (Exception ex) {
            System.out.println("SQLException trying to get comments for twitter");
            ex.printStackTrace();
            throw new SQLException("SQLException trying to get comments for twitter");
        }
    }

    public Comment getById(Integer id) {
        Comment comment = null;
        try (Session session = sessionFactory.openSession()) {
            comment = session.get(Comment.class, id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return comment;
    }
}
