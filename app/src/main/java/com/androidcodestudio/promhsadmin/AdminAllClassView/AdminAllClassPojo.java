package com.androidcodestudio.promhsadmin.AdminAllClassView;

public class AdminAllClassPojo {


    private String categoryName;
    private String categoryIconLink;
    private String backgroundColor;
    private int index;
    private String key;

    public AdminAllClassPojo() {
    }

    public AdminAllClassPojo(String categoryName, String categoryIconLink, String backgroundColor, int index, String key) {
        this.categoryName = categoryName;
        this.categoryIconLink = categoryIconLink;
        this.backgroundColor = backgroundColor;
        this.index = index;
        this.key = key;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryIconLink() {
        return categoryIconLink;
    }

    public void setCategoryIconLink(String categoryIconLink) {
        this.categoryIconLink = categoryIconLink;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
