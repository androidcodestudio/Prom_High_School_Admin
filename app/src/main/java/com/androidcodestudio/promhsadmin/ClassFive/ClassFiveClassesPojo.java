package com.androidcodestudio.promhsadmin.ClassFive;

public class ClassFiveClassesPojo {
    private String name;
    private int set;
    private String key;

    public ClassFiveClassesPojo() {
    }

    public ClassFiveClassesPojo(String name, int set, String key) {
        this.name = name;
        this.set = set;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
