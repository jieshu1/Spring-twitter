package com.jie.twitter.dao;

import com.jie.twitter.entity.Tweet;
import com.jie.twitter.entity.User;
import com.jie.twitter.entity.UserProfile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserProfileDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void create(UserProfile userProfile) throws Exception{
        Session session = null;
        try{
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(userProfile);
            session.getTransaction().commit();
            System.out.println(String.format("user profile created: %s", userProfile.toString()));
        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
            throw new SQLException("SQL Exception for creating User Profile: " + userProfile.toString());
        } finally {
            if (session != null){
                session.close();
            }
        }
    }

    public void update(UserProfile userProfile) throws Exception{
        Session session = null;
        try{
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.update(userProfile);
            session.getTransaction().commit();
            System.out.println(String.format("user profile updated: %s", userProfile.toString()));
        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
            throw new SQLException("SQL Exception for updating" +
                    " User Profile: " + userProfile.toString());
        } finally {
            if (session != null){
                session.close();
            }
        }
    }

    public UserProfile getByEmail(String email) {
        UserProfile userProfile = null;
        try (Session session = sessionFactory.openSession()) {
            userProfile = session.get(UserProfile.class, email);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userProfile;
    }
}
