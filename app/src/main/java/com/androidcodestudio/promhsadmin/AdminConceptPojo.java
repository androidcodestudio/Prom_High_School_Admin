package com.androidcodestudio.promhsadmin;

public class AdminConceptPojo {
    private String id,ConceptTitle,ConceptUrl;
    private int set;

    public AdminConceptPojo() {
    }

    public AdminConceptPojo(String id, String conceptTitle, String conceptUrl, int set) {
        this.id = id;
        ConceptTitle = conceptTitle;
        ConceptUrl = conceptUrl;
        this.set = set;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConceptTitle() {
        return ConceptTitle;
    }

    public void setConceptTitle(String conceptTitle) {
        ConceptTitle = conceptTitle;
    }

    public String getConceptUrl() {
        return ConceptUrl;
    }

    public void setConceptUrl(String conceptUrl) {
        ConceptUrl = conceptUrl;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }
}
