package com.idreameducation.ipreppal.model;

import java.io.Serializable;

/**
 * Created by apple on 03/02/18.
 */

public class FacilitatorStudentModel implements Serializable {
    public String getFacilitatorId() {
        return facilitatorId;
    }

    public void setFacilitatorId(String facilitatorId) {
        this.facilitatorId = facilitatorId;
    }

    private String facilitatorId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name,className;
    private String Subject;
    private String userType;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getBatchID() {
        return batchID;
    }

    public void setBatchID(String batchID) {
        this.batchID = batchID;
    }

    private String batchID;

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    private String teacherName;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
