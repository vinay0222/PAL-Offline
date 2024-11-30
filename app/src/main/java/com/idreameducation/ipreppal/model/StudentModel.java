package com.idreameducation.ipreppal.model;

public class StudentModel {

    String AcademicYear,AllocatedSubjects, sClass,DateOfBirth
            ,DateofAdmission,Gender,IsActive,SRN,SchoolBlockName,SchoolCode,SchoolDistrictName
            ,SchoolName,Section,Stream
            ,StudentName,UDISECode;

    Long authTime,lastLoginTime;
    String idreamEmail;

    public StudentModel(String academicYear, String allocatedSubjects, String sClass, String dateOfBirth, String dateofAdmission, String gender, String isActive, String SRN, String schoolBlockName, String schoolCode, String schoolDistrictName, String schoolName, String section, String stream, String studentName, String UDISECode) {
        AcademicYear = academicYear;
        AllocatedSubjects = allocatedSubjects;
        this.sClass = sClass;
        DateOfBirth = dateOfBirth;
        DateofAdmission = dateofAdmission;
        Gender = gender;
        IsActive = isActive;
        this.SRN = SRN;
        SchoolBlockName = schoolBlockName;
        SchoolCode = schoolCode;
        SchoolDistrictName = schoolDistrictName;
        SchoolName = schoolName;
        Section = section;
        Stream = stream;
        StudentName = studentName;
        this.UDISECode = UDISECode;
    }

    public StudentModel(String academicYear, String allocatedSubjects, String aClass, String dateOfBirth, String dateofAdmission, String gender, String isActive, String SRN, String schoolBlockName, String schoolCode, String schoolDistrictName, String schoolName, String section, String stream, String studentName, String UDISECode, Long authTime, Long lastLoginTime, String idreamEmail) {
        AcademicYear = academicYear;
        AllocatedSubjects = allocatedSubjects;
        sClass = aClass;
        DateOfBirth = dateOfBirth;
        DateofAdmission = dateofAdmission;
        Gender = gender;
        IsActive = isActive;
        this.SRN = SRN;
        SchoolBlockName = schoolBlockName;
        SchoolCode = schoolCode;
        SchoolDistrictName = schoolDistrictName;
        SchoolName = schoolName;
        Section = section;
        Stream = stream;
        StudentName = studentName;
        this.UDISECode = UDISECode;
        this.authTime = authTime;
        this.lastLoginTime = lastLoginTime;
        this.idreamEmail = idreamEmail;
    }

    public StudentModel() {
    }

    public String getAcademicYear() {
        return AcademicYear;
    }

    public void setAcademicYear(String academicYear) {
        AcademicYear = academicYear;
    }

    public String getAllocatedSubjects() {
        return AllocatedSubjects;
    }

    public void setAllocatedSubjects(String allocatedSubjects) {
        AllocatedSubjects = allocatedSubjects;
    }

    public String getsClass() {
        return sClass;
    }

    public void setsClass(String sClass) {
        this.sClass = sClass;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getDateofAdmission() {
        return DateofAdmission;
    }

    public void setDateofAdmission(String dateofAdmission) {
        DateofAdmission = dateofAdmission;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getSRN() {
        return SRN;
    }

    public void setSRN(String SRN) {
        this.SRN = SRN;
    }

    public String getSchoolBlockName() {
        return SchoolBlockName;
    }

    public void setSchoolBlockName(String schoolBlockName) {
        SchoolBlockName = schoolBlockName;
    }

    public String getSchoolCode() {
        return SchoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        SchoolCode = schoolCode;
    }

    public String getSchoolDistrictName() {
        return SchoolDistrictName;
    }

    public void setSchoolDistrictName(String schoolDistrictName) {
        SchoolDistrictName = schoolDistrictName;
    }

    public String getSchoolName() {
        return SchoolName;
    }

    public void setSchoolName(String schoolName) {
        SchoolName = schoolName;
    }

    public String getSection() {
        return Section;
    }

    public void setSection(String section) {
        Section = section;
    }

    public String getStream() {
        return Stream;
    }

    public void setStream(String stream) {
        Stream = stream;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getUDISECode() {
        return UDISECode;
    }

    public void setUDISECode(String UDISECode) {
        this.UDISECode = UDISECode;
    }

    public Long getAuthTime() {
        return authTime;
    }

    public void setAuthTime(Long authTime) {
        this.authTime = authTime;
    }

    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getIdreamEmail() {
        return idreamEmail;
    }

    public void setIdreamEmail(String idreamEmail) {
        this.idreamEmail = idreamEmail;
    }
}

