package com.jie.twitter.dao;

import com.jie.twitter.entity.Tweet;
import com.jie.twitter.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TweetDao {

    @Autowired
    private SessionFactory sessionFactory;

    public Boolean createTweet(Tweet tweet){
        Boolean result = false;
        Session session = null;
        try{
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(tweet);
            session.getTransaction().commit();
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if (session != null){
                session.close();
            }
        }
        return result;
    }

    public Boolean getTweets(User user){
        Boolean result = false;
        List<Tweet> tweetsList = new ArrayList<Tweet>();
        try (Session session = sessionFactory.openSession()) {
            String hql = "from Tweet tweet where tweet.user=:user";
            Query theQuery = session.createQuery(hql);
            theQuery.setParameter("user", user);
            tweetsList = theQuery.getResultList();
            if (tweetsList.size() > 0){
                System.out.println("userID:" + user.getEmail() +
                        ", has tweets: before saving to database:" + user.getTweetsList());
                user.setTweetsList(tweetsList);
                System.out.println("userID:" + user.getEmail() +
                        ", has tweets: after saving to database" + user.getTweetsList());
            }
            else {
                System.out.println("userID:" + user.getEmail() +
                        ", has no tweets");
            }
            result = true;
            return result;
        } catch (Exception ex) {
            System.out.println("Exception trying to get tweets from user");
            ex.printStackTrace();
        }
        return result;
    }
}
