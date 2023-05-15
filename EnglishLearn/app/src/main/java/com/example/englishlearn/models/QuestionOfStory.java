package com.example.englishlearn.models;

public class QuestionOfStory {
    private int id;
    private int idstory;
    private String content;

    public QuestionOfStory() {
    }

    public QuestionOfStory(int id, int idstory, String content) {
        this.id = id;
        this.idstory = idstory;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdstory() {
        return idstory;
    }

    public void setIdstory(int idstory) {
        this.idstory = idstory;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
