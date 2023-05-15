package com.example.englishlearn.models;

public class QuestionOfTest {
    private int id;
    private String idtest;
    private String content;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private int goodanswer;

    public QuestionOfTest() {
    }

    public QuestionOfTest(int id, String idtest, String content,
                          String answer1, String answer2, String answer3,
                          String answer4, int goodanswer) {
        this.id = id;
        this.idtest = idtest;
        this.content = content;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.goodanswer = goodanswer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdtest() {
        return idtest;
    }

    public void setIdtest(String idtest) {
        this.idtest = idtest;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public int getGoodanswer() {
        return goodanswer;
    }

    public void setGoodanswer(int goodanswer) {
        this.goodanswer = goodanswer;
    }
}
