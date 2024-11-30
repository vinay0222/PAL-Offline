package com.idreameducation.ipreppal.PalMobile.Models;

import java.util.ArrayList;

public class ProjectTopicModel {

    String name,key;

    ArrayList<ProjectSubTopicModel> topics;

    public ProjectTopicModel(String name, ArrayList<ProjectSubTopicModel> topics) {
        this.name = name;
        this.topics = topics;
    }

    public ProjectTopicModel() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ProjectSubTopicModel> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<ProjectSubTopicModel> topics) {
        this.topics = topics;
    }
}

