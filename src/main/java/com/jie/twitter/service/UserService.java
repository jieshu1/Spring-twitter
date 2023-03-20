package com.jie.twitter.service;
import com.jie.twitter.dao.UserDao;
import com.jie.twitter.dao.UserProfileDao;
import com.jie.twitter.dao.UserSessionDao;
import com.jie.twitter.entity.User;
import com.jie.twitter.entity.UserProfile;
import com.jie.twitter.entity.UserSession;
import com.jie.twitter.exception.UserNotFoundException;
import com.jie.twitter.utils.EmailFormat;
import com.jie.twitter.utils.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserSessionDao userSessionDao;

    @Autowired
    private UserProfileDao userProfileDao;

    public void signUp(User user)
    {
        EmailFormat.setEmailFormat(user);
        user.setEnabled(true);
        user.setPassword(Encrypt.passwordEncrypt(user.getPassword(), user.getEmail()));
        System.out.println("signup service, password encrypted:" + user.getPassword() +
                "for user:" + user.getEmail());
        userDao.signUp(user);
    }

    public User getUser(String email) {
        return userDao.getUser(email);
    }

    public UserProfile getUserProfile(UserProfile userProfile) {
        EmailFormat.setEmailFormat(userProfile);
        UserProfile result = userProfileDao.getByEmail(userProfile.getEmail());
        return result;
    }

    public Boolean validateUser(User user){
        EmailFormat.setEmailFormat(user);
        user.setPassword(Encrypt.passwordEncrypt(user.getPassword(), user.getEmail()));
        return userDao.validateUser(user);
    }

    public Boolean validateUsername(User user) {
        EmailFormat.setEmailFormat(user);
        return userDao.validateUserName(user.getEmail());
    }

    public void validateEmail(String email) throws Exception{
        User user = new User();
        user.setEmail(email);
        EmailFormat.setEmailFormat(user);
        if (userDao.validateUserName(user.getEmail()) == false) {
            throw new UserNotFoundException(email);
        }
    }

    public UserSession login(User user){
        EmailFormat.setEmailFormat(user);
        UserSession userSession = userSessionDao.getUserSession(user);
        if (userSession == null){
            System.out.println("userService: userSession was not found, about to creat new session");
            userSession = new UserSession();
            userSession.setId();
            userSession.setUser(user);
        }
        userSession.setIsActive(true);
        userSessionDao.login(userSession);
        return userSession;
    }

    public User logout(UUID id){
        UserSession userSession = userSessionDao.getUserSession(id);
        User user = null;
        if (userSession!=null){
            user = userSession.getUser();
            System.out.println("about to logout userSession:" + userSession.getId() + " " + userSession.getUser().getEmail() + " " + userSession.getIsActive());
            userSession.setIsActive(false);
            System.out.println("set isActive to false:" + userSession.getIsActive());
            userSessionDao.update(userSession);
            userSession = userSessionDao.getUserSession(id);
            user = userSession.getUser();
            System.out.println("after logout user" + user.getEmail() + " " + userSession.getIsActive());
        }
        return user;
    }

    public void updateUserProfile(UserProfile userProfile) throws Exception {
        EmailFormat.setEmailFormat(userProfile);
        User user = new User();
        user.setEmail(userProfile.getEmail());
        userProfile.setUser(user);
        if (userDao.validateUserName(userProfile.getEmail()) == false){
            throw new UserNotFoundException(userProfile.getEmail());
        }
        UserProfile oldUserProfile = userProfileDao.getByEmail(userProfile.getEmail());
        if(userProfile.getAvatar() == null) {
            userProfile.setAvatar(oldUserProfile.getAvatar());
        }
        if(userProfile.getNickname() == null) {
            userProfile.setNickname(oldUserProfile.getNickname());
        }
        if (oldUserProfile == null){
            userProfileDao.create(userProfile);
        }
        else {
            userProfileDao.update(userProfile);
        }
    }
}
