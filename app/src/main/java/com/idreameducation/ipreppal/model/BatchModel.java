package com.idreameducation.ipreppal.model;

public class BatchModel {

    String boardID,classID,className,createdAt,language,shareCode,subject,teacherID,teacherName;

    public BatchModel(String boardID, String classID, String className, String createdAt, String language, String shareCode, String subject, String teacherID, String teacherName) {
        this.boardID = boardID;
        this.classID = classID;
        this.className = className;
        this.createdAt = createdAt;
        this.language = language;
        this.shareCode = shareCode;
        this.subject = subject;
        this.teacherID = teacherID;
        this.teacherName = teacherName;
    }

    public BatchModel() {
    }

    public String getBoardID() {
        return boardID;
    }

    public void setBoardID(String boardID) {
        this.boardID = boardID;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
