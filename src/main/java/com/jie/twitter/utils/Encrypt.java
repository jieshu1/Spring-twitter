package com.jie.twitter.utils;


import org.apache.commons.codec.digest.DigestUtils;

public class Encrypt {
    private static final String SERVER_SECRETKEY = "K2391F192alc93719b30nI39581";

    public static String passwordEncrypt(String password, String email){
        return DigestUtils.md5Hex(password + email.toLowerCase() + SERVER_SECRETKEY);
    }
}
