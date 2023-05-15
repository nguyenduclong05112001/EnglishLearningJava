package com.example.englishlearn.models;

public class InfoPoinOfDay {
    private String day;
    private int point;
    private int chains;
    private int pointofweek;

    public InfoPoinOfDay() {
    }

    public InfoPoinOfDay(String day, int point, int chains, int pointofweek) {
        this.day = day;
        this.point = point;
        this.chains = chains;
        this.pointofweek = pointofweek;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getChains() {
        return chains;
    }

    public void setChains(int chains) {
        this.chains = chains;
    }

    public int getPointofweek() {
        return pointofweek;
    }

    public void setPointofweek(int pointofweek) {
        this.pointofweek = pointofweek;
    }
}
