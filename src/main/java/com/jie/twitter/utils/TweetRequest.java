package com.jie.twitter.utils;

import org.springframework.core.style.ToStringCreator;

public class TweetRequest {
    public String email;
    public String content;

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("email", this.email)
                .append("content", this.content)
                .toString();
    }


}
