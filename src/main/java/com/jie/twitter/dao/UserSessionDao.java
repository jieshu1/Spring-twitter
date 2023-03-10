package com.jie.twitter.dao;

import com.jie.twitter.entity.User;
import com.jie.twitter.entity.UserSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

@Repository
public class UserSessionDao {
    @Autowired
    private SessionFactory sessionFactory;

    public UserSession getUserSession(User user) {
        UserSession userSession = null;
        try (Session session = sessionFactory.openSession()) {
            String hql = "from UserSession userSession where userSession.user=:user";
            Query theQuery = session.createQuery(hql);
            theQuery.setParameter("user", user);
            List<UserSession> results = theQuery.getResultList();
            if (results.size() > 0){
                userSession = results.get(0);
                System.out.println("UserSession found trough userID:" + userSession.getUser() +
                        ", Session ID:" + userSession.getId());;
            }
            else {
                System.out.println("UserSession not found trough userID");
            }
            return userSession;

        } catch (Exception ex) {
            System.out.println("Exception trying to get session from user");
            ex.printStackTrace();
        }
        return userSession;
    }

    public void login(UserSession userSession) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(userSession);
            session.getTransaction().commit();
            System.out.println("Session created for user:" + userSession.getUser() + ", Session ID:" + userSession.getId());
        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public UserSession getUserSession(UUID id) {
        UserSession userSession = null;
        try (Session session = sessionFactory.openSession()) {
            userSession = session.get(UserSession.class, id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userSession;
    }
    public void update(UserSession userSession){
        System.out.println("about to update userSession:" + userSession.getId() + ", isActive:" + userSession.getIsActive());
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.update(userSession);
            session.getTransaction().commit();
            System.out.println("successfully updated userSession");
        } catch (Exception ex){
            System.out.println("error updating userSession");
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


}
