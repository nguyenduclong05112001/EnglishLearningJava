package com.example.englishlearn.models;

public class AnswerOfStory {
    private int id;
    private int idstory;
    private int idquestion;
    private String content;
    private int statusanswer;

    public AnswerOfStory() {
    }

    public AnswerOfStory(int id, int idstory, int idquestion, String content, int statusanswer) {
        this.id = id;
        this.idstory = idstory;
        this.idquestion = idquestion;
        this.content = content;
        this.statusanswer = statusanswer;
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

    public int getIdquestion() {
        return idquestion;
    }

    public void setIdquestion(int idquestion) {
        this.idquestion = idquestion;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatusanswer() {
        return statusanswer;
    }

    public void setStatusanswer(int statusanswer) {
        this.statusanswer = statusanswer;
    }
}
