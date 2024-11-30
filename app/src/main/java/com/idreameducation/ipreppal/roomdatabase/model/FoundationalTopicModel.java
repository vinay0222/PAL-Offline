package com.idreameducation.ipreppal.roomdatabase.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class FoundationalTopicModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "topic_id")
    private String topicId;
    @ColumnInfo(name = "topic_name")
    private String topicName;
    @ColumnInfo(name = "sClass")
    private String sClass;
    @ColumnInfo(name = "streak_progress")
    private String streakProgress;
    @ColumnInfo(name = "streak_count")
        private String streakCount;
    @ColumnInfo(name = "incorrect_streak")
    private String incorrectStreak;
    @ColumnInfo(name = "practice_type")
    private String practiceType;
    @ColumnInfo(name = "senior_class")
    private String seniorClass;
    @ColumnInfo(name = "senior_topic_id")
    private String seniorTopicID;
    @ColumnInfo(name = "senior_topic_name")
    private String seniorTopicName;
    @ColumnInfo(name = "show")
    private Boolean show;
    @ColumnInfo(name = "video_level")
    private Integer videoLevel;
    @ColumnInfo(name = "test_percentage_achieved")
    private Integer testPercentageAchieved;
    @ColumnInfo(name = "user_id")
    private String userId;
    @ColumnInfo(name = "subject_id")
    private String subjectId;
    @ColumnInfo(name = "lang")
    private String lang;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getSClass() {
        return sClass;
    }

    public void setSClass(String sClass) {
        this.sClass = sClass;
    }

    public String getStreakProgress() {
        return streakProgress;
    }

    public void setStreakProgress(String streakProgress) {
        this.streakProgress = streakProgress;
    }

    public String getStreakCount() {
        return streakCount;
    }

    public void setStreakCount(String streakCount) {
        this.streakCount = streakCount;
    }

    public String getIncorrectStreak() {
        return incorrectStreak;
    }

    public void setIncorrectStreak(String incorrectStreak) {
        this.incorrectStreak = incorrectStreak;
    }

    public String getPracticeType() {
        return practiceType;
    }

    public void setPracticeType(String practiceType) {
        this.practiceType = practiceType;
    }

    public String getSeniorClass() {
        return seniorClass;
    }

    public void setSeniorClass(String seniorClass) {
        this.seniorClass = seniorClass;
    }

    public String getSeniorTopicID() {
        return seniorTopicID;
    }

    public void setSeniorTopicID(String seniorTopicID) {
        this.seniorTopicID = seniorTopicID;
    }

    public String getSeniorTopicName() {
        return seniorTopicName;
    }

    public void setSeniorTopicName(String seniorTopicName) {
        this.seniorTopicName = seniorTopicName;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public Integer getVideoLevel() {
        return videoLevel;
    }

    public void setVideoLevel(Integer videoLevel) {
        this.videoLevel = videoLevel;
    }

    public Integer getTestPercentageAchieved() {
        return testPercentageAchieved;
    }

    public void setTestPercentageAchieved(Integer testPercentageAchieved) {
        this.testPercentageAchieved = testPercentageAchieved;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
