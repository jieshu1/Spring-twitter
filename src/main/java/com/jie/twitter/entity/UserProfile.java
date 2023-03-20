package com.jie.twitter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user_profiles")
public class UserProfile implements Serializable {
    @Id
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @MapsId
    @OneToOne
    @JoinColumn(name = "email")
    @JsonIgnore
    private User user;

    @Column(length = 511, nullable = true)
    private String avatar;

    @Column(length = 255, nullable = true)
    private String nickname;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String toString() {
        return new ToStringCreator(this)
                .append("email", this.email)
                .append("avatar", this.avatar)
                .append("nickname",this.nickname)
                .toString();
    }
}
