package com.jie.twitter.utils;

import com.jie.twitter.entity.User;
import com.jie.twitter.entity.UserProfile;

public class EmailFormat {
    public static void setEmailFormat(User user){
        user.setEmail(user.getEmail().toLowerCase());
    }

    public static void setEmailFormat(UserProfile userProfile){
        userProfile.setEmail(userProfile.getEmail().toLowerCase());
    }

}
