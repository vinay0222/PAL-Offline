package com.idreameducation.ipreppal.model;

public class StudentInfoModel {

    String userName,userClass, userMobile,userRollNo,userLanguage;

    public StudentInfoModel(String userName, String userClass, String userMobile, String userRollNo, String userLanguage) {
        this.userName = userName;
        this.userClass = userClass;
        this.userMobile = userMobile;
        this.userRollNo = userRollNo;
        this.userLanguage = userLanguage;
    }

    public StudentInfoModel() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserRollNo() {
        return userRollNo;
    }

    public void setUserRollNo(String userRollNo) {
        this.userRollNo = userRollNo;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }
}
