package com.idreameducation.ipreppal.model;

import java.io.Serializable;

/**
 * Created by nice on 30-Oct-17.
 */

public class RegisterModel implements Serializable {

    private String State;
    private String email;
    private String age;
    private String Mobile;
    private String userType;
    private String isLicenseVerificationCompleted;
    private String studentClass;
    private String fullName;
    private String isSDCARDAvailable = "False";
    private String appID;
    private String language;
    private String educationBoard;
    private String rollNo;
    private String ngoID;
    private String token;
    private String dateStarted;
    private String mobile;
    private String City;
    private String boardID;
    private String packageLanguage;
    private String classID;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEducationBoard() {
        return educationBoard;
    }

    public void setEducationBoard(String educationBoard) {
        this.educationBoard = educationBoard;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNgoID() {
        return ngoID;
    }

    public void setNgoID(String ngoID) {
        this.ngoID = ngoID;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getIsSDCARDAvailable() {
        return isSDCARDAvailable;
    }

    public void setIsSDCARDAvailable(String isSDCARDAvailable) {
        this.isSDCARDAvailable = isSDCARDAvailable;
    }


    public String getIsLicenseVerificationCompleted() {
        return isLicenseVerificationCompleted;
    }

    public void setIsLicenseVerificationCompleted(String isLicenseVerificationCompleted) {
        this.isLicenseVerificationCompleted = isLicenseVerificationCompleted;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
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

    public String getPackageLanguage() {
        return packageLanguage;
    }

    public void setPackageLanguage(String packageLanguage) {
        this.packageLanguage = packageLanguage;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }
}
