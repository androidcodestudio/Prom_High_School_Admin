package com.androidcodestudio.promhsadmin.intro;

public class IntroPojo {

    String Title,Description;
    int ScreenImages;

    public IntroPojo(String title, String description, int screenImages) {
        Title = title;
        Description = description;
        ScreenImages = screenImages;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getScreenImages() {
        return ScreenImages;
    }

    public void setScreenImages(int screenImages) {
        ScreenImages = screenImages;
    }
}
