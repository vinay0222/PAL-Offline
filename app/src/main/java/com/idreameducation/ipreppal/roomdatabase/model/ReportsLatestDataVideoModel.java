package com.idreameducation.ipreppal.roomdatabase.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class ReportsLatestDataVideoModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "user_id")
    private String userId;
    @ColumnInfo(name = "board")
    private String board;
    @ColumnInfo(name = "sClass")
    private String sClass;
    @ColumnInfo(name = "subject")
    private String subject;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "topic_id")
    private String topicId;
    @ColumnInfo(name = "v_id")
    private String vId;
    @ColumnInfo(name = "url")
    private String url;
    @ColumnInfo(name = "v_time")
    private String vTime;
    @ColumnInfo(name = "topic_name")
    private String topicName;
    @ColumnInfo(name = "total_time")
    private String totalTime;
    @ColumnInfo(name = "video_name")
    private String videoName;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getSClass() {
        return sClass;
    }

    public void setSClass(String sClass) {
        this.sClass = sClass;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getVId() {
        return vId;
    }

    public void setVId(String vId) {
        this.vId = vId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVTime() {
        return vTime;
    }

    public void setVTime(String vTime) {
        this.vTime = vTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}