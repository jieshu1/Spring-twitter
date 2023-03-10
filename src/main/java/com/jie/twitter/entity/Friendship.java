package com.jie.twitter.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "friendships", indexes = {
        @Index(name = "from_user_to_user", columnList = "from_user, to_user", unique = true),
        @Index(name = "from_user_created_at", columnList = "from_user, created_at DESC"),
        @Index(name = "to_user_created_at", columnList = "to_user, created_at DESC"),
})
public class Friendship {
    @Id
    @Column(name = "id")
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "from_user")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user")
    private User toUser;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public Integer getId(){
        return id;
    }
    public void setFromUser(User user){
        this.fromUser = user;
    }
    public User getFromUser(){
        return fromUser;
    }

    public void setToUser(User user){
        this.toUser = user;
    }
    public User getToUser(){
        return toUser;
    }

    public Date getCreatedAt(){
        return createdAt;
    }
    public Date getUpdatedAt(){return updatedAt;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("fromUser", this.fromUser)
                .append("toUser", this.toUser)
                .append("createdAt", this.createdAt)
                .append("updatedAt", this.updatedAt)
                .toString();
    }


}
