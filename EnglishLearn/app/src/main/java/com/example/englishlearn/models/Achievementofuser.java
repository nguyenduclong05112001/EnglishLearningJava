package com.example.englishlearn.models;

public class Achievementofuser {
    private String username;
    private int level;
    private int part;
    private int chain;
    private int pointofday;
    private int pointofweek;

    public Achievementofuser() {

    }

    public Achievementofuser(String username, int level, int part, int chain, int pointofday, int pointofweek) {
        this.username = username;
        this.level = level;
        this.part = part;
        this.chain = chain;
        this.pointofday = pointofday;
        this.pointofweek = pointofweek;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public int getChain() {
        return chain;
    }

    public void setChain(int chain) {
        this.chain = chain;
    }

    public int getPointofday() {
        return pointofday;
    }

    public void setPointofday(int pointofday) {
        this.pointofday = pointofday;
    }

    public int getPointofweek() {
        return pointofweek;
    }

    public void setPointofweek(int pointofweek) {
        this.pointofweek = pointofweek;
    }
}
