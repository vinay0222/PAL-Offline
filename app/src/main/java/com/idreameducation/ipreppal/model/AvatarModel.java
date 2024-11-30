package com.idreameducation.ipreppal.model;

public class AvatarModel {

    String id,link;
    boolean visable;

    public AvatarModel(String id, String link, boolean visable) {
        this.id = id;
        this.link = link;
        this.visable = visable;
    }

    public AvatarModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isVisable() {
        return visable;
    }

    public void setVisable(boolean visable) {
        this.visable = visable;
    }
}
