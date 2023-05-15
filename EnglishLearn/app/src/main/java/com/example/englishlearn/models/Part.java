package com.example.englishlearn.models;

import java.io.Serializable;

public class Part implements Serializable {
    private int id;
    private int idlevel;
    private String name;
    private String avatar;
    private int status;

    public Part() {
    }

    public Part(int id, int idlevel, String name, String avatar, int status) {
        this.id = id;
        this.idlevel = idlevel;
        this.name = name;
        this.avatar = avatar;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdlevel() {
        return idlevel;
    }

    public void setIdlevel(int idlevel) {
        this.idlevel = idlevel;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
