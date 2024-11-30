package com.idreameducation.ipreppal.roomdatabase.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class ReportsTestScoreModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int testId;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "questions_attempted")
    private String questionsAttempted;
    @ColumnInfo(name = "scores")
    private String scores;
    @ColumnInfo(name = "topic_name")
    private String topicName;
    @ColumnInfo(name = "total_questions")
    private String totalQuestions;
    @ColumnInfo(name = "total_scores")
    private String totalScores;
    @ColumnInfo(name = "percentage_scored")
    private String percentageScored;
//    @ColumnInfo(name = "lang")
//    private String lang;
//
//    public String getLang() {
//        return lang;
//    }
//
//    public void setLang(String lang) {
//        this.lang = lang;
//    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public String getQuestionsAttempted() {
        return questionsAttempted;
    }

    public void setQuestionsAttempted(String questionsAttempted) {
        this.questionsAttempted = questionsAttempted;
    }

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(String totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalScores() {
        return totalScores;
    }

    public void setTotalScores(String totalScores) {
        this.totalScores = totalScores;
    }

    public String getPercentageScored() {
        return percentageScored;
    }

    public void setPercentageScored(String percentageScored) {
        this.percentageScored = percentageScored;
    }
}