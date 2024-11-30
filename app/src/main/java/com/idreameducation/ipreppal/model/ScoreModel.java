package com.idreameducation.ipreppal.model;

import java.io.Serializable;

/**
 * Created by apple on 30/12/17.
 */

public class ScoreModel implements Serializable {
    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }

    private String scores;
    private String time;
    private String seektime;

    public String getSeektime() {
        return seektime;
    }

    public void setSeektime(String seektime) {
        this.seektime = seektime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    private String videoName;

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
    private String subjectName;


    public String getTotalScores() {
        return totalScores;
    }

    public void setTotalScores(String totalScores) {
        this.totalScores = totalScores;
    }

    private String totalScores;
    private String totalQuestions;

    public String getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(String totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getQuestionsAttempted() {
        return questionsAttempted;
    }

    public void setQuestionsAttempted(String questionsAttempted) {
        this.questionsAttempted = questionsAttempted;
    }

    private String questionsAttempted;

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }
    private String totalTime;

    public String getTimeConsume() {
        return timeConsume;
    }

    public void setTimeConsume(String timeConsume) {
        this.timeConsume = timeConsume;
    }
    private String timeConsume;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
