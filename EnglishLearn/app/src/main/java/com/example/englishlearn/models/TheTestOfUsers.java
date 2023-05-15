package com.example.englishlearn.models;

import java.io.Serializable;

public class TheTestOfUsers implements Serializable {
    private String id;
    private String iduser;
    private String name;
    private String avatar;
    private String datecreate;

    public TheTestOfUsers() {
    }

    public TheTestOfUsers(String id, String iduser, String name, String avatar, String datecreate) {
        this.id = id;
        this.iduser = iduser;
        this.name = name;
        this.avatar = avatar;
        this.datecreate = datecreate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
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

    public String getDatecreate() {
        return datecreate;
    }

    public void setDatecreate(String datecreate) {
        this.datecreate = datecreate;
    }
}
