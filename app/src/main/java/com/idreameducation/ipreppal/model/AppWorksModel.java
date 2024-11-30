package com.idreameducation.ipreppal.model;

public class AppWorksModel {

    private String title;
    private String text;
    private int image;
    private boolean expanded;

    public AppWorksModel(String title, String text, int image) {
        this.title = title;
        this.text = text;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

}
