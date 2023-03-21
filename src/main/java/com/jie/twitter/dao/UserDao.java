package com.jie.twitter.dao;


import com.jie.twitter.entity.Authorities;
import com.jie.twitter.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void signUp(User user) {
        Authorities authorities = new Authorities();
        authorities.setAuthorities("ROLE_USER");
        authorities.setEmail(user.getEmail());

        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(authorities);
            session.save(user);
            session.getTransaction().commit();
            System.out.println("UserDao, customer and authority created:" + user.getEmail());
        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public User getUser(String email) {
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            user = session.get(User.class, email);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }

    public Boolean validateUser(User user) {
        Boolean result = false;
        try (Session session = sessionFactory.openSession()) {
            Query theQuery = session.createQuery("from User u where u.email=:email AND u.password=:password");
            theQuery.setParameter("email", user.getEmail());
            theQuery.setParameter("password", user.getPassword());
            if (theQuery.getResultList().size() == 0){
                ;
            }
            else {
                result = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Boolean validateUserName(String email) {
        Boolean result = false;
        try (Session session = sessionFactory.openSession()) {
            Query theQuery = session.createQuery("from User u where u.email=:email");
            theQuery.setParameter("email", email);
            if (theQuery.getResultList().size() != 0){
                System.out.println("DAO validated username:" + email);
                result = true;
            }
            else{
                System.out.println("DAO failed to validate username:" + email);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }


}
