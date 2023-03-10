package com.jie.twitter.utils;

import com.jie.twitter.entity.User;

public class EmailFormat {
    public static void setEmailFormat(User user){
        user.setEmail(user.getEmail().toLowerCase());
    }
}
