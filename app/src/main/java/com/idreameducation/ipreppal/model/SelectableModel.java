package com.idreameducation.ipreppal.model;

public class SelectableModel {

    String name;
    boolean selectable=false;

    public SelectableModel(String name, boolean selectable) {
        this.name = name;
        this.selectable = selectable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }
}
