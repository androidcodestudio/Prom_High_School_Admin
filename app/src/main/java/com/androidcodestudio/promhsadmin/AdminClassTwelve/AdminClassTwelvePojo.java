package com.androidcodestudio.promhsadmin.AdminClassTwelve;

public class AdminClassTwelvePojo {
    private String name;
    private int set;
    private String url;
    private String key;

    public AdminClassTwelvePojo() {
    }

    public AdminClassTwelvePojo(String name, int set, String url, String key) {
        this.name = name;
        this.set = set;
        this.url = url;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
