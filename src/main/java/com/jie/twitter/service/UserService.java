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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @CachePut(value = "memCache", key = "'user:'.concat(#user.getEmail())")
    public User signUp(User user)
    {
        EmailFormat.setEmailFormat(user);
        user.setEnabled(true);
        user.setPassword(Encrypt.passwordEncrypt(user.getPassword(), user.getEmail()));
        System.out.println("signup service, password encrypted:" + user.getPassword() +
                "for user:" + user.getEmail());
        userDao.signUp(user);
        return user;
    }
    @Cacheable(value = "memCache", key = "'user:'.concat(#email)")
    public User getUser(String email) {
        System.out.println("get user through database");
        return userDao.getUser(email);
    }

    @Cacheable(value = "memCache", key = "'userprofile:'.concat(#email)")
    public UserProfile getUserProfile(String email) {
        System.out.println("get user profile through database");
        UserProfile result = userProfileDao.getByEmail(email);
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
    public UserSession login(String email){
        User user = new User();
        user.setEmail(email);
        UserSession userSession = userSessionDao.getUserSession(email);
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

    @CacheEvict(value = "memCache", key = "'userprofile:'.concat(#userProfile.getEmail())")
    public void updateUserProfile(UserProfile userProfile) throws Exception {
        User user = new User();
        user.setEmail(userProfile.getEmail());
        userProfile.setUser(user);
        if (userDao.validateUserName(userProfile.getEmail()) == false){
            throw new UserNotFoundException(userProfile.getEmail());
        }
        UserProfile oldUserProfile = getUserProfile(userProfile.getEmail());
        if (oldUserProfile == null){
            userProfileDao.create(userProfile);
        }
        else {
            userProfileDao.update(userProfile);
        }
    }
}
