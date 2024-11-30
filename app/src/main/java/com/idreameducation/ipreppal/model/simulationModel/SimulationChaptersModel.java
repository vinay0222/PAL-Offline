package com.idreameducation.ipreppal.model.simulationModel;

import java.util.ArrayList;

public class SimulationChaptersModel {

    String name;
    ArrayList<SimulationTopicsModel> topics;

    public SimulationChaptersModel(String name, ArrayList<SimulationTopicsModel> topics) {
        this.name = name;
        this.topics = topics;
    }

    public SimulationChaptersModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SimulationTopicsModel> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<SimulationTopicsModel> topics) {
        this.topics = topics;
    }
}
