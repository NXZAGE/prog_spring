package com.itmo.nxzage.server.services.db.models;

public class Credential {
    private Integer userId;
    private String passwordHash;
    private String salt;

    public Credential(Integer userId, String passwordHash, String salt) {
        this.userId = userId;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    } 

    
}
