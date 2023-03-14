package com.jie.twitter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tweets", indexes = @Index(name = "user_created_at",
        columnList = "user_id, created_at DESC", unique = true))
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    @NotNull
    private Integer id;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @Column(length = 255)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    //@JsonIgnore
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    List<Newsfeed> newsfeedList;

    public Integer getId(){
        return id;
    }
    public void setUser(User user){
        this.user = user;
    }
    public User getUser(){
        return user;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return content;
    }

    public Date getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setNewsfeedList(List<Newsfeed> newsfeedList) {
        this.newsfeedList = newsfeedList;
    }

    public List<Newsfeed> getNewsfeedList() {
        if (newsfeedList == null) {
            newsfeedList = new ArrayList<>();
        }
        return newsfeedList;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("user", this.user)
                .append("content", this.content)
                .append("createdAt", this.createdAt)
                .toString();
    }

}
