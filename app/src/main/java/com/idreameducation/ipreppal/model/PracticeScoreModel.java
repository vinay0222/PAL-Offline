package com.idreameducation.ipreppal.model;

public class PracticeScoreModel {

    private String topicId, score,streakProgress,currentLevel;

    public PracticeScoreModel() {
    }

    public PracticeScoreModel(String topicId, String score, String streakProgress, String currentLevel) {
        this.topicId = topicId;
        this.score = score;
        this.streakProgress = streakProgress;
        this.currentLevel = currentLevel;
    }

    public String getStreakProgress() {
        return streakProgress;
    }

    public void setStreakProgress(String streakProgress) {
        this.streakProgress = streakProgress;
    }

    public String getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(String currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
