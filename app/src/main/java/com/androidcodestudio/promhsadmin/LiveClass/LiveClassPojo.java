package com.androidcodestudio.promhsadmin.LiveClass;

public class LiveClassPojo {
    private String UpcomingClassName;
    private String Date;
    private String Time;

    private String key;

    public LiveClassPojo() {
    }

    public LiveClassPojo(String upcomingClassName, String date, String time, String key) {
        UpcomingClassName = upcomingClassName;
        Date = date;
        Time = time;
        this.key = key;
    }

    public String getUpcomingClassName() {
        return UpcomingClassName;
    }

    public void setUpcomingClassName(String upcomingClassName) {
        UpcomingClassName = upcomingClassName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
