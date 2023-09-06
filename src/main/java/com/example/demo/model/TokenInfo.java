package com.example.demo.model;

public class TokenInfo {
    private String token;
    private String userName;
    private long createTime;

    public TokenInfo(String token, String userName, long createTime) {
        this.token = token;
        this.userName = userName;
        this.createTime = createTime;
    }

    public String getToken() {
        return token;
    }

    public String getUserName() {
        return userName;
    }

    public long getCreateTime() {
        return createTime;
    }
}
