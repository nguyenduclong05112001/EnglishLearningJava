package com.example.englishlearn.models;

public class User {
    private String username;
    private String name;
    private String avatar;
    private String password;

    public User(String username, String name, String avatar, String password) {
        this.username = username;
        this.name = name;
        this.avatar = avatar;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
