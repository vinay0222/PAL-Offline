package com.idreameducation.ipreppal.roomdatabase.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class PracticeModel implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private String display;
    private String Foundational_Topic_ID;
    private String IsModelTestPaper;
    private String Levels;
    private String StreakCount;
    private String TName;
    private String TName_alt;
    private String TopicID;
    private String incorrectStreak;
    private String subject;
    private String language;
    private String studentClass;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;
    private String board;
    private String categoryID;
    private String isAlternateLanguageAvailable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getFoundational_Topic_ID() {
        return Foundational_Topic_ID;
    }

    public void setFoundational_Topic_ID(String foundational_Topic_ID) {
        Foundational_Topic_ID = foundational_Topic_ID;
    }

    public String getIsModelTestPaper() {
        return IsModelTestPaper;
    }

    public void setIsModelTestPaper(String isModelTestPaper) {
        IsModelTestPaper = isModelTestPaper;
    }

    public String getLevels() {
        return Levels;
    }

    public void setLevels(String levels) {
        Levels = levels;
    }

    public String getStreakCount() {
        return StreakCount;
    }

    public void setStreakCount(String streakCount) {
        StreakCount = streakCount;
    }

    public String getTName() {
        return TName;
    }

    public void setTName(String TName) {
        this.TName = TName;
    }

    public String getTName_alt() {
        return TName_alt;
    }

    public void setTName_alt(String TName_alt) {
        this.TName_alt = TName_alt;
    }

    public String getTopicID() {
        return TopicID;
    }

    public void setTopicID(String topicID) {
        TopicID = topicID;
    }

    public String getIncorrectStreak() {
        return incorrectStreak;
    }

    public void setIncorrectStreak(String incorrectStreak) {
        this.incorrectStreak = incorrectStreak;
    }

    public String getIsAlternateLanguageAvailable() {
        return isAlternateLanguageAvailable;
    }

    public void setIsAlternateLanguageAvailable(String isAlternateLanguageAvailable) {
        this.isAlternateLanguageAvailable = isAlternateLanguageAvailable;
    }


}
