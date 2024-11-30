package com.idreameducation.ipreppal.model;

import java.util.ArrayList;

public class AboutPALModel {
    boolean selected;
    String id,pdfSolution,language,question,section,videoSolution;
    String answerKey;

    ArrayList<String> tags;

    Long notUseful,useful;

    public AboutPALModel(String id, String language, String question, String section, ArrayList<String> tags, String answerKey, Long notUseful, Long useful) {
        this.id = id;
        this.language = language;
        this.question = question;
        this.section = section;
        this.tags = tags;
        this.answerKey = answerKey;
        this.notUseful = notUseful;
        this.useful = useful;
    }

    public AboutPALModel( String id, String pdfSolution, String language, String question, String section, ArrayList<String> tags, String videoSolution, Long notUseful, Long useful) {
        this.id = id;
        this.pdfSolution = pdfSolution;
        this.language = language;
        this.question = question;
        this.section = section;
        this.tags = tags;
        this.videoSolution = videoSolution;
        this.notUseful = notUseful;
        this.useful = useful;
    }

    public AboutPALModel() {
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPdfSolution() {
        return pdfSolution;
    }

    public void setPdfSolution(String pdfSolution) {
        this.pdfSolution = pdfSolution;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getVideoSolution() {
        return videoSolution;
    }

    public void setVideoSolution(String videoSolution) {
        this.videoSolution = videoSolution;
    }

    public Long getNotUseful() {
        return notUseful;
    }

    public void setNotUseful(Long notUseful) {
        this.notUseful = notUseful;
    }

    public Long getUseful() {
        return useful;
    }

    public void setUseful(Long useful) {
        this.useful = useful;
    }
}
