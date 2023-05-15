package com.example.englishlearn.models;

import java.io.Serializable;

public class Level implements Serializable {
    private int id;
    private String name;
    private String avatar;
    private int sumpart;

    public Level() {
    }

    public Level(int id, String name, String avatar, int sumpart) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.sumpart = sumpart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getSumpart() {
        return sumpart;
    }

    public void setSumpart(int sumpart) {
        this.sumpart = sumpart;
    }
}
