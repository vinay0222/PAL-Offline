package com.idreameducation.ipreppal.model.simulationModel;

import java.util.ArrayList;

/** Use this model for Simulation and Audio Book content Mapping */
public class SimulationSubjectModel {

    String subjectName;
    String color,icon,id,name,short_name;

    ArrayList<SimulationChaptersModel> chapters;

    public SimulationSubjectModel(ArrayList<SimulationChaptersModel> chapters) {
        this.chapters = chapters;
    }

    public SimulationSubjectModel() {
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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public ArrayList<SimulationChaptersModel> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<SimulationChaptersModel> chapters) {
        this.chapters = chapters;
    }
}
