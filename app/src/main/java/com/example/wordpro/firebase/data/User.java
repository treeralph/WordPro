package com.example.wordpro.firebase.data;

import java.util.HashMap;

public class User extends Data{

    String nickname;
    String deviceToken;
    String additional;

    public User(){

    }
    public User(String nickname, String deviceToken, String additional) {
        this.nickname = nickname;
        this.deviceToken = deviceToken;
        this.additional = additional;
    }

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                ", additional='" + additional + '\'' +
                '}';
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }
}
