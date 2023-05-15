package com.example.englishlearn.models;

public class Answer {
    private int id;
    private int idquestion;
    private String content;

    public Answer() {
    }

    public Answer(int id, int idquestion, String content) {
        this.id = id;
        this.idquestion = idquestion;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
