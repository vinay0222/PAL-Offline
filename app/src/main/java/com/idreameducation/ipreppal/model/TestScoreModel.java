package com.idreameducation.ipreppal.model;

public class TestScoreModel {

    private String topicId, type, score, percentage,time;

    public TestScoreModel() {
    }

    public TestScoreModel(String topicId, String type, String score, String percentage) {
        this.topicId = topicId;
        this.type = type;
        this.score = score;
        this.percentage = percentage;
    }

    public TestScoreModel(String topicId, String type, String score, String percentage, String time) {
        this.topicId = topicId;
        this.type = type;
        this.score = score;
        this.percentage = percentage;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
