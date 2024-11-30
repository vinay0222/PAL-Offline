package com.idreameducation.ipreppal.roomdatabase.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class TrackTopicModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "user_id")
    private String userId;
    @ColumnInfo(name = "subject")
    private String subject;
    @ColumnInfo(name = "board")
    private String board;
    @ColumnInfo(name = "senior_topic_id")
    private String seniorTopicId;
    @ColumnInfo(name = "senior_class")
    private String seniorClass;
    @ColumnInfo(name = "senior_topic_name")
    private String seniorTopicName;
    @ColumnInfo(name = "foundational_topic_id")
    private String foundationalTopicId;
    @ColumnInfo(name = "language")
    private String language;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getSeniorTopicId() {
        return seniorTopicId;
    }

    public void setSeniorTopicId(String seniorTopicId) {
        this.seniorTopicId = seniorTopicId;
    }

    public String getSeniorClass() {
        return seniorClass;
    }

    public void setSeniorClass(String seniorClass) {
        this.seniorClass = seniorClass;
    }

    public String getSeniorTopicName() {
        return seniorTopicName;
    }

    public void setSeniorTopicName(String seniorTopicName) {
        this.seniorTopicName = seniorTopicName;
    }

    public String getFoundationalTopicId() {
        return foundationalTopicId;
    }

    public void setFoundationalTopicId(String foundationalTopicId) {
        this.foundationalTopicId = foundationalTopicId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}