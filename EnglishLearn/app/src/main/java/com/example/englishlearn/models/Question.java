package com.example.englishlearn.models;

public class Question {
    private int id;
    private int idpart;
    private String content;

    public Question() {
    }

    public Question(int id, int idpart, String content) {
        this.id = id;
        this.idpart = idpart;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdpart() {
        return idpart;
    }

    public void setIdpart(int idpart) {
        this.idpart = idpart;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
