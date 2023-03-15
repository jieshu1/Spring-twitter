package com.jie.twitter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "newsfeeds", indexes = @Index(name = "user_created_at",
        columnList = "user_id, created_at DESC", unique = true))
public class Newsfeed implements Serializable {
    @Id
    @NotNull
    @Column(name = "id")
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    //@JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "tweet")
    private Tweet tweet;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    public Integer getId(){
        return id;
    }
    public void setUser(User user){
        this.user = user;
    }
    public User getUser(){
        return user;
    }

    public void setTweet(Tweet tweet){
        this.tweet = tweet;
    }

    public Tweet getTweet(){
        return tweet;
    }

    public Date getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("user", this.user)
                .append("tweet", this.tweet)
                .append("createdAt", this.createdAt)
                .toString();
    }


}
