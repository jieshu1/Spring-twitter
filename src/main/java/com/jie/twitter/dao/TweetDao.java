package com.jie.twitter.dao;

import com.jie.twitter.entity.Comment;
import com.jie.twitter.entity.Tweet;
import com.jie.twitter.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TweetDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void createTweet(Tweet tweet) throws Exception{
        Session session = null;
        try{
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(tweet);
            session.getTransaction().commit();
            System.out.println(String.format("tweet created: %s", tweet.toString()));
        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
            throw new SQLException("SQL Exception for creating tweet: " + tweet.toString());
        } finally {
            if (session != null){
                session.close();
            }
        }
    }

    public void getTweets(User user) throws Exception{
        List<Tweet> tweetsList = new ArrayList<Tweet>();
        try (Session session = sessionFactory.openSession()) {
            String hql = "from Tweet tweet where tweet.user=:user";
            Query theQuery = session.createQuery(hql);
            theQuery.setParameter("user", user);
            tweetsList = theQuery.getResultList();
            if (tweetsList.size() > 0){
                user.setTweetsList(tweetsList);
                System.out.println("userID:" + user.getEmail() +
                        ", has tweets: after saving to database" + user.getTweetsList());
            }
            else {
                System.out.println("userID:" + user.getEmail() +
                        ", has no tweets");
            }
        } catch (Exception ex) {
            System.out.println("Exception trying to get tweets from user");
            ex.printStackTrace();
            throw new SQLException("SQL Exception to get tweets from user:" + user.getEmail());
        }
    }

    public Tweet getById(Integer id) {
        Tweet tweet = null;
        try (Session session = sessionFactory.openSession()) {
            tweet = session.get(Tweet.class, id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tweet;
    }
}
