package com.jie.twitter.service;

import com.jie.twitter.dao.FriendshipDao;
import com.jie.twitter.entity.Friendship;
import com.jie.twitter.utils.EmailFormat;
import com.jie.twitter.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendshipService {
    @Autowired
    private FriendshipDao friendshipDao;

    private EmailFormat emailFormat = new EmailFormat();

    public Boolean follow(Friendship friendship){
        emailFormat.setEmailFormat(friendship.getFromUser());
        emailFormat.setEmailFormat(friendship.getToUser());
        if(friendship.getFromUser().getEmail().equals(friendship.getToUser().getEmail())){
            return false;
        }
        if (friendshipDao.isFriend(friendship)) {
            return true;
        }
        else{
            return friendshipDao.follow(friendship);
        }
    }

    public Boolean isFriend(Friendship friendship) {
        emailFormat.setEmailFormat(friendship.getFromUser());
        emailFormat.setEmailFormat(friendship.getToUser());
        return friendshipDao.isFriend(friendship);
    }

    public Boolean unfollow(Friendship friendship){
        emailFormat.setEmailFormat(friendship.getFromUser());
        emailFormat.setEmailFormat(friendship.getToUser());
        if (friendshipDao.isFriend(friendship)) {
            return friendshipDao.unfollow(friendship);
        }
        else{
            return true;
        }
    }

    public Boolean getFollowers(User user){
        emailFormat.setEmailFormat(user);
        List<User> followersList = new ArrayList<>();
        user.setFollowersList(followersList);
        return friendshipDao.getFollowers(user);
    }

    public Boolean getFollowings(User user){
        emailFormat.setEmailFormat(user);
        List<User> followingsList = new ArrayList<>();
        user.setFollowingsList(followingsList);
        return friendshipDao.getFollowings(user);
    }


}
