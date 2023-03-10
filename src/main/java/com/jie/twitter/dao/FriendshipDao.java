package com.jie.twitter.dao;

import com.jie.twitter.entity.Friendship;
import com.jie.twitter.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FriendshipDao {
    @Autowired
    private SessionFactory sessionFactory;

    public boolean follow(Friendship friendship){
        Boolean result = false;
        Session session = null;
        try{
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(friendship);
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

    public boolean unfollow(Friendship friendship){
        Boolean result = false;
        Session session = null;
        try{
            session = sessionFactory.openSession();
            session.beginTransaction();
            Query theQuery = session.createQuery("delete from Friendship f where f.fromUser=:fromUser AND f.toUser=:toUser");
            theQuery.setParameter("fromUser", friendship.getFromUser());
            theQuery.setParameter("toUser", friendship.getToUser());
            int rows = theQuery.executeUpdate();
            session.getTransaction().commit();

            if (rows > 0){
                System.out.println("friendship record deleted:" + rows);
                result = true;
            }
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


    public boolean getFollowers(User user){
        Boolean result = false;
        Session session = null;
        try{
            session = sessionFactory.openSession();
            Query theQuery = session.createQuery("from Friendship f where f.toUser=:toUser");
            theQuery.setParameter("toUser", user);
            List<Friendship> friendshipsList = theQuery.getResultList();
            if (friendshipsList.size() > 0){
                for(int i = 0; i < friendshipsList.size(); i++){
                    User fromUser = new User();
                    fromUser.setEmail(friendshipsList.get(i).getFromUser().getEmail());
                    user.addUserToFollowersList(fromUser);
                }
                System.out.println("followers list:" + user.getFollowersList().toString());
            }
            else{
                System.out.println("followers list is empty");
            }
            result = true;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (session != null){
                session.close();
            }
        }
        return result;
    }

    public boolean isFriend(Friendship friendship){
        Boolean result = false;
        Session session = null;
        try{
            session = sessionFactory.openSession();
            Query theQuery = session.createQuery("from Friendship f where f.toUser=:toUser AND f.fromUser=:fromUser");
            theQuery.setParameter("toUser", friendship.getToUser());
            theQuery.setParameter("fromUser", friendship.getFromUser());
            List<Friendship> friendshipsList = theQuery.getResultList();
            if (friendshipsList.size() > 0){
                System.out.println("is friend");
                result = true;
            }
            else{
                System.out.println("is not friend");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (session != null){
                session.close();
            }
        }
        return result;
    }

    public boolean getFollowings(User user){
        Boolean result = false;
        Session session = null;
        try{
            session = sessionFactory.openSession();
            Query theQuery = session.createQuery("from Friendship f where f.fromUser=:fromUser");
            theQuery.setParameter("fromUser", user);
            List<Friendship> friendshipsList = theQuery.getResultList();
            if (friendshipsList.size() > 0){
                for(int i = 0; i < friendshipsList.size(); i++){
                    User toUser = new User();
                    toUser.setEmail(friendshipsList.get(i).getToUser().getEmail());
                    user.addUserToFollowingsList(toUser);
                }
                System.out.println("followings list:" + user.getFollowingsList().toString());
            }
            else{
                System.out.println("followings list is empty");
            }
            result = true;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (session != null){
                session.close();
            }
        }
        return result;
    }
}
