package com.preppilot.api.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "leetcode_username", length = 30)
    private String leetcodeUsername;

    @Column(name = "full_name", length = 80)
    private String fullName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "mobile", length = 10)
    private String mobile;

    @Column(name = "avatar_url")
    private String avatarUrl;

    protected AppUser() {
    }

    public AppUser(UUID id, String email, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getLeetcodeUsername() {
        return leetcodeUsername;
    }

    public void linkLeetcode(String username, Instant now) {
        this.leetcodeUsername = username;
        this.updatedAt = now;
    }

    public void unlinkLeetcode(Instant now) {
        this.leetcodeUsername = null;
        this.updatedAt = now;
    }

    public String getFullName() {
        return fullName;
    }

    public Integer getAge() {
        return age;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void updateProfile(String fullName, Integer age, String mobile, String avatarUrl, Instant now) {
        this.fullName = fullName;
        this.age = age;
        this.mobile = mobile;
        this.avatarUrl = avatarUrl;
        this.updatedAt = now;
    }
}
