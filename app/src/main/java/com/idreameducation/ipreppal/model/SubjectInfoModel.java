package com.idreameducation.ipreppal.model;

public class SubjectInfoModel {

    String color,icon,id,name,short_name,visibility;

    public SubjectInfoModel(String color, String icon, String id, String name, String short_name, String visibility) {
        this.color = color;
        this.icon = icon;
        this.id = id;
        this.name = name;
        this.short_name = short_name;
        this.visibility = visibility;
    }

    public SubjectInfoModel() {
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }
}
