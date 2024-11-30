package com.idreameducation.ipreppal.model;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInfoModel {

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

    public UserInfoModel(String state, String email, String age, String mobile, String userType, String isLicenseVerificationCompleted, String studentClass, String fullName, String isSDCARDAvailable, String appID, String language, String educationBoard, String rollNo, String ngoID, String token, String dateStarted, String mobile1, String city, String boardID, String packageLanguage, String classID) {
        State = state;
        this.email = email;
        this.age = age;
        Mobile = mobile;
        this.userType = userType;
        this.isLicenseVerificationCompleted = isLicenseVerificationCompleted;
        this.studentClass = studentClass;
        this.fullName = fullName;
        this.isSDCARDAvailable = isSDCARDAvailable;
        this.appID = appID;
        this.language = language;
        this.educationBoard = educationBoard;
        this.rollNo = rollNo;
        this.ngoID = ngoID;
        this.token = token;
        this.dateStarted = dateStarted;
        this.mobile = mobile1;
        City = city;
        this.boardID = boardID;
        this.packageLanguage = packageLanguage;
        this.classID = classID;
    }

    public UserInfoModel(String UID) {
        FirebaseFirestore.getInstance().collection("PAL_UserInfo").document(UID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            UserInfoModel userInfoModel=new UserInfoModel();
                            userInfoModel.setAge(documentSnapshot.getString("age"));
                            userInfoModel.setState(documentSnapshot.getString("State"));
                            userInfoModel.setEmail(documentSnapshot.getString("email"));
                            userInfoModel.setMobile(documentSnapshot.getString("Mobile"));
                            userInfoModel.setUserType(documentSnapshot.getString("userType"));
                            userInfoModel.setIsLicenseVerificationCompleted(documentSnapshot.getString("isLicenseVerificationCompleted"));
                            userInfoModel.setStudentClass(documentSnapshot.getString("studentClass"));
                            userInfoModel.setFullName(documentSnapshot.getString("fullName"));
                            userInfoModel.setIsSDCARDAvailable(documentSnapshot.getString("isSDCARDAvailable"));
                            userInfoModel.setAppID(documentSnapshot.getString("appID"));
                            userInfoModel.setLanguage(documentSnapshot.getString("language"));
                            userInfoModel.setEducationBoard(documentSnapshot.getString("educationBoard"));
                            userInfoModel.setRollNo(documentSnapshot.getString("rollNo"));
                            userInfoModel.setNgoID(documentSnapshot.getString("ngoID"));
                            userInfoModel.setToken(documentSnapshot.getString("token"));
                            userInfoModel.setDateStarted(documentSnapshot.getString("dateStarted"));
                            userInfoModel.setMobile(documentSnapshot.getString("mobile"));
                            userInfoModel.setCity(documentSnapshot.getString("City"));
                            userInfoModel.setBoardID(documentSnapshot.getString("boardID"));
                            userInfoModel.setPackageLanguage(documentSnapshot.getString("packageLanguage"));
                            userInfoModel.setClassID(documentSnapshot.getString("classID"));
                        }
                        else new UserInfoModel();
                    }
                });
    }

    public UserInfoModel() {
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
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

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getBoardID() {
        return boardID;
    }

    public void setBoardID(String boardID) {
        this.boardID = boardID;
    }

    public String getPackageLanguage() {
        return packageLanguage;
    }

    public void setPackageLanguage(String packageLanguage) {
        this.packageLanguage = packageLanguage;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getIsLicenseVerificationCompleted() {
        return isLicenseVerificationCompleted;
    }

    public void setIsLicenseVerificationCompleted(String isLicenseVerificationCompleted) {
        this.isLicenseVerificationCompleted = isLicenseVerificationCompleted;
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

    public String getIsSDCARDAvailable() {
        return isSDCARDAvailable;
    }

    public void setIsSDCARDAvailable(String isSDCARDAvailable) {
        this.isSDCARDAvailable = isSDCARDAvailable;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

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

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getNgoID() {
        return ngoID;
    }

    public void setNgoID(String ngoID) {
        this.ngoID = ngoID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }
}
