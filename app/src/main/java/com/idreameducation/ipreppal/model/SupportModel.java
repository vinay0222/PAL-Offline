package com.idreameducation.ipreppal.model;

public class SupportModel {

    String userToken;
    String user_id;
    String device_id;
    String user_class;
    String user_board;
    String user_language;
    String date_of_reported_issue;
    String timestamp;
    String text_by_users;
    String image_by_users;
    String status;
    String user_name;
    String district;
    String school_name;
    String state;
    String issue_type;
    String project_name;
    String schoolID;
    String projectId;

    String remark;

    String key;

    boolean selected;

    public SupportModel(String userToken, String user_id, String device_id, String user_class, String user_board, String user_language, String date_of_reported_issue, String timestamp, String text_by_users, String image_by_users, String status, String user_name, String district, String school_name, String state, String issue_type, String project_name, String schoolID, String projectId, String remark, String key, boolean selected) {
        this.userToken = userToken;
        this.user_id = user_id;
        this.device_id = device_id;
        this.user_class = user_class;
        this.user_board = user_board;
        this.user_language = user_language;
        this.date_of_reported_issue = date_of_reported_issue;
        this.timestamp = timestamp;
        this.text_by_users = text_by_users;
        this.image_by_users = image_by_users;
        this.status = status;
        this.user_name = user_name;
        this.district = district;
        this.school_name = school_name;
        this.state = state;
        this.issue_type = issue_type;
        this.project_name = project_name;
        this.schoolID = schoolID;
        this.projectId = projectId;
        this.remark = remark;
        this.key = key;
        this.selected = selected;
    }

    public SupportModel() {
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getUser_class() {
        return user_class;
    }

    public void setUser_class(String user_class) {
        this.user_class = user_class;
    }

    public String getUser_board() {
        return user_board;
    }

    public void setUser_board(String user_board) {
        this.user_board = user_board;
    }

    public String getUser_language() {
        return user_language;
    }

    public void setUser_language(String user_language) {
        this.user_language = user_language;
    }

    public String getDate_of_reported_issue() {
        return date_of_reported_issue;
    }

    public void setDate_of_reported_issue(String date_of_reported_issue) {
        this.date_of_reported_issue = date_of_reported_issue;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getText_by_users() {
        return text_by_users;
    }

    public void setText_by_users(String text_by_users) {
        this.text_by_users = text_by_users;
    }

    public String getImage_by_users() {
        return image_by_users;
    }

    public void setImage_by_users(String image_by_users) {
        this.image_by_users = image_by_users;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIssue_type() {
        return issue_type;
    }

    public void setIssue_type(String issue_type) {
        this.issue_type = issue_type;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
