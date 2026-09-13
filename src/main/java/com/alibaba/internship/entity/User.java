package com.alibaba.internship.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User entity — maps to the `user` table.
 * Migrated from Oracle VARCHAR2/NUMBER/DATE to MySQL VARCHAR/DECIMAL/DATETIME.
 */
public class User {

    private Long id;
    private String userId;
    private String email;
    private String password;
    private String nickName;
    private String gender;
    private String avatarUrl;
    private BigDecimal balance;
    private String paykey;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getPaykey() { return paykey; }
    public void setPaykey(String paykey) { this.paykey = paykey; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
