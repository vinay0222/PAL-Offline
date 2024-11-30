package com.idreameducation.ipreppal.model;

public class SubjectsModel {

    String SubjectName;
    SubjectInfoModel subjectInfo;

    public SubjectsModel() {
    }

    public SubjectsModel(String subjectName, SubjectInfoModel subjectInfo) {
        SubjectName = subjectName;
        this.subjectInfo = subjectInfo;
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    public SubjectInfoModel getSubjectInfo() {
        return subjectInfo;
    }

    public void setSubjectInfo(SubjectInfoModel subjectInfo) {
        this.subjectInfo = subjectInfo;
    }

}
