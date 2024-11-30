package com.idreameducation.ipreppal.model;

import java.io.Serializable;

/**
 * Created by apple on 26/06/18.
 */

public class ContentErrorModel implements Serializable {

    private String topicId;
    private String questionId;
    private String message;
    private String reason4;
    private String reason3;
    private String reason2;
    private String board;
    private String sClass;

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getsClass() {
        return sClass;
    }

    public void setsClass(String sClass) {
        this.sClass = sClass;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    private String language;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    private String level;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;


    public long getTimeOfReport() {
        return timeOfReport;
    }

    public void setTimeOfReport(long timeOfReport) {
        this.timeOfReport = timeOfReport;
    }

    private long timeOfReport;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    public String getReason4() {
        return reason4;
    }

    public void setReason4(String reason4) {
        this.reason4 = reason4;
    }

    public String getReason3() {
        return reason3;
    }

    public void setReason3(String reason3) {
        this.reason3 = reason3;
    }

    public String getReason2() {
        return reason2;
    }

    public void setReason2(String reason2) {
        this.reason2 = reason2;
    }

    public String getReason1() {
        return reason1;
    }

    public void setReason1(String reason1) {
        this.reason1 = reason1;
    }

    private String reason1;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
