package com.idreameducation.ipreppal.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by apple on 03/02/18.
 */

public class FacilitatorClassModel implements Serializable {
    private String code;

    String createdAt;

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    private String teacherID;

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }

    private String teacherCode;

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    private String teacherName;

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    private String shareCode;
    private String Language;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    private String className;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    private String link;

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    private String boardID;

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    private String boardName;

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

    private String classID;

    ArrayList<HashMap<String,String>> subjectList;

    public ArrayList<HashMap<String, String>> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(ArrayList<HashMap<String, String>> subjectList) {
        this.subjectList = subjectList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    private String subject;

    public FacilitatorClassModel() {
    }

    public FacilitatorClassModel(String code, String createdAt, String teacherID, String teacherCode, String teacherName, String shareCode, String language, String className, String link, String boardID, String boardName, String classID, ArrayList<HashMap<String, String>> subjectList, String name, String subject) {
        this.code = code;
        this.createdAt = createdAt;
        this.teacherID = teacherID;
        this.teacherCode = teacherCode;
        this.teacherName = teacherName;
        this.shareCode = shareCode;
        Language = language;
        this.className = className;
        this.link = link;
        this.boardID = boardID;
        this.boardName = boardName;
        this.classID = classID;
        this.subjectList = subjectList;
        this.name = name;
        this.subject = subject;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
