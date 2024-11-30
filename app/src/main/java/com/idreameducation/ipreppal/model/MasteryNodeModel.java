package com.idreameducation.ipreppal.model;

import java.io.Serializable;

/**
 * Created by nice on 30-Oct-17.
 */

public class MasteryNodeModel implements Serializable {


    public String getMastery() {
        return mastery;
    }

    public void setMastery(String mastery) {
        this.mastery = mastery;
    }

    public String getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(String currentLevel) {
        this.currentLevel = currentLevel;
    }

    private String mastery;
   private String currentLevel;
   private String time;

    public String getStreakProgress() {
        return streakProgress;
    }

    public void setStreakProgress(String streakProgress) {
        this.streakProgress = streakProgress;
    }

    private String streakProgress;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    private String topicName;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFoundationalLevel() {
        return foundationalLevel;
    }

    public void setFoundationalLevel(String foundationalLevel) {
        this.foundationalLevel = foundationalLevel;
    }

    private String foundationalLevel;







}
