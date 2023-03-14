package com.jie.twitter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 2652327633296064143L;

    @Id
    private String email;

    private String firstName;

    private String lastName;

    @JsonIgnore
    private String password;

    private boolean enabled;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private UserSession userSession;

    @JsonIgnore
    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Tweet> tweetsList;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Newsfeed> newsfeedsList;

    @JsonIgnore
    @Transient
    private List<User> followersList;

    @JsonIgnore
    @Transient
    private List<User> followingsList;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public UserSession getSession(){
        return userSession;
    }

    public Date getCreatedAt(){
        return createdAt;
    }

    public List<Tweet> getTweetsList(){
        if (this.tweetsList == null) {this.tweetsList = new ArrayList<>();}
        return tweetsList;
    }
    public void setTweetsList(List<Tweet> tweetsList) {
        this.tweetsList = tweetsList;
    }

    public List<Newsfeed> getNewsfeedsList(){
        if (this.newsfeedsList == null) {
            this.newsfeedsList = new ArrayList<>();
        }
        return newsfeedsList;
    }
    public void setNewsfeedsList(List<Newsfeed> newsfeedsList) {
        this.newsfeedsList = newsfeedsList;
    }

    public List<User> getFollowersList(){
        if (followersList == null) {
            followersList = new ArrayList<>();
        }
        return followersList;
    }

    public void setFollowersList(List<User> followersList){this.followersList = followersList;}

    public void addUserToFollowersList(User user) {
        if (this.followersList == null){
            this.followersList = new LinkedList<User>();
        }
        this.followersList.add(user);
    }

    public List<User> getFollowingsList(){
        if(followingsList == null) {
            followingsList = new ArrayList<>();
        }
        return followingsList;
    }

    public void setFollowingsList(List<User> followingsList){this.followingsList = followingsList;}

    public void addUserToFollowingsList(User user) {
        if (this.followingsList == null){
            this.followingsList = new LinkedList<User>();
        }
        this.followingsList.add(user);
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }


    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("email", this.email)
                .toString();
    }
}


