package com.example.englishlearn.models;

import java.io.Serializable;

public class Story implements Serializable {
    private int id;
    private String name;
    private String content;
    private String avatar;

    public Story() {
    }

    public Story(int id, String name, String content, String avatar) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.avatar = avatar;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
