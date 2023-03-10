package com.jie.twitter.dao;

import com.jie.twitter.entity.User;
import com.jie.twitter.entity.Newsfeed;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NewsfeedDao {

    @Autowired
    private SessionFactory sessionFactory;

    public boolean createNewsfeed(Newsfeed newsfeed){
        Boolean result = false;
        Session session = null;
        try{
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(newsfeed);
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

    public void bulkCreateNewsfeeds(List<Newsfeed> newsfeedsList, int batchSize){
        Session session = null;
        try{
            session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            for (int i = 0; i < newsfeedsList.size(); i++) {
                if (i > 0 && i % batchSize == 0) {
                    session.flush();
                    session.clear();
                    System.out.println("Successfully Fan out newsfeed to " + i +
                            "th follower:" + newsfeedsList.get(i));
                }
                session.save(newsfeedsList.get(i));
            }
            tx.commit();
            System.out.println("Successfully Fan out newsfeed all followers");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (session != null){
                session.close();
            }
        }
    }

    public Boolean getNewsfeeds(User user){
        Boolean result = false;
        List<Newsfeed> newsfeedsList = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            String hql = "from Newsfeed newsfeed where newsfeed.user=:user";
            Query theQuery = session.createQuery(hql);
            theQuery.setParameter("user", user);
            newsfeedsList = theQuery.getResultList();
            if (newsfeedsList.size() > 0){
                System.out.println("userID:" + user.getEmail() +
                        ", has newsfeeds: before saving to database:" + user.getNewsfeedsList());
                user.setNewsfeedsList(newsfeedsList);
                System.out.println("userID:" + user.getEmail() +
                        ", has newsfeeds: after saving to database" + user.getNewsfeedsList());
            }
            else {
                System.out.println("userID:" + user.getEmail() +
                        ", has no newsfeeds");
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
