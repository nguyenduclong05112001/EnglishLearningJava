package com.example.englishlearn.models;

import java.io.Serializable;

public class TheTestLocal implements Serializable {
    private String id;
    private String content;
    private int numberQuestion;


    public TheTestLocal() {
    }

    public TheTestLocal(String id, String content, int numberQuestion) {
        this.id = id;
        this.content = content;
        this.numberQuestion = numberQuestion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNumberQuestion() {
        return numberQuestion;
    }

    public void setNumberQuestion(int numberQuestion) {
        this.numberQuestion = numberQuestion;
    }
}
