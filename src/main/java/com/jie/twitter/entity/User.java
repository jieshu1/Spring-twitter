package com.jie.twitter.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import java.io.Serializable;
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

    private String password;

    private boolean enabled;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private UserSession userSession;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    private List<Tweet> tweetsList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    private List<Newsfeed> newsfeedsList;

    @Transient
    private List<User> followersList;

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
        return tweetsList;
    }
    public void setTweetsList(List<Tweet> tweetsList) {
        this.tweetsList = tweetsList;
    }

    public List<Newsfeed> getNewsfeedsList(){
        return newsfeedsList;
    }
    public void setNewsfeedsList(List<Newsfeed> newsfeedsList) {
        this.newsfeedsList = newsfeedsList;
    }

    public List<User> getFollowersList(){
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
        return followingsList;
    }

    public void setFollowingsList(List<User> followingsList){this.followingsList = followingsList;}

    public void addUserToFollowingsList(User user) {
        if (this.followingsList == null){
            this.followingsList = new LinkedList<User>();
        }
        this.followingsList.add(user);
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("email", this.email)
                .toString();
    }
}


