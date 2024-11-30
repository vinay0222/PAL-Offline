package com.idreameducation.ipreppal.model;

/**
 * Created by Sandeep on 11-01-2018.
 */

public class StudentInfo {
    public String studentName;
    public String studentPassword;
    public String username;
    public String sClass;
    public String sBoard;

    public String getsClass() {
        return sClass;
    }

    public void setsClass(String sClass) {
        this.sClass = sClass;
    }

    public String getsBoard() {
        return sBoard;
    }

    public void setsBoard(String sBoard) {
        this.sBoard = sBoard;
    }

    public String getsLanguage() {
        return sLanguage;
    }

    public void setsLanguage(String sLanguage) {
        this.sLanguage = sLanguage;
    }

    public String sLanguage;
    public boolean activeStatus = true;

    public StudentInfo(String studentName, String studentPassword, String username) {
        this.studentName = studentName;
        this.studentPassword = studentPassword;
        this.username = username;
    }

    public boolean getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStudentPassword() {
        return studentPassword;
    }

    public void setStudentPassword(String studentPassword) {
        this.studentPassword = studentPassword;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
